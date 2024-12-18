
package org.jclouds.s3.filters;

import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteStreams;
import org.gaul.modernizer_maven_annotations.SuppressModernizer;
import org.jclouds.http.HttpException;
import org.jclouds.io.MutableContentMetadata;
import org.jclouds.io.Payload;
import org.jclouds.io.payloads.BaseMutableContentMetadata;
import org.jclouds.io.payloads.BasePayload;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.ByteStreams.readBytes;
import static org.jclouds.s3.filters.Aws4SignerBase.hash;
import static org.jclouds.s3.filters.Aws4SignerBase.hex;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.CHUNK_SIGNATURE_HEADER;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.CHUNK_STRING_TO_SIGN_PREFIX;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.CLRF;
import static org.jclouds.s3.filters.AwsSignatureV4Constants.FINAL_CHUNK;
import static org.jclouds.util.Strings2.toInputStream;

public class ChunkedUploadPayload extends BasePayload<Payload> {
    private static final byte[] TRAILER = CLRF.getBytes(UTF_8);
    private final Payload payload;
    private final int chunkedBlockSize;
    private final String timestamp;
    private final String scope;
    private final ByteProcessor<byte[]> hmacSHA256;
    private String lastComputedSignature;

    public ChunkedUploadPayload(Payload payload, int blockSize, String timestamp, String scope,
                                ByteProcessor<byte[]> hmacSHA256, String seedSignature) {
        super(payload);
        this.payload = payload;
        this.chunkedBlockSize = blockSize;
        this.timestamp = timestamp;
        this.scope = scope;
        this.hmacSHA256 = hmacSHA256;
        this.lastComputedSignature = seedSignature;

        MutableContentMetadata contentMetadata = BaseMutableContentMetadata.fromContentMetadata(
                payload.getContentMetadata());
        long totalLength = Aws4SignerForChunkedUpload.calculateChunkedContentLength(
                payload.getContentMetadata().getContentLength(),
                chunkedBlockSize);
        contentMetadata.setContentLength(totalLength);
        this.setContentMetadata(contentMetadata);
    }

    protected byte[] constructSignedChunk(int userDataLen, byte[] userData) {
        byte[] dataToChunk = prepareDataToChunk(userDataLen, userData);
        String chunkStringToSign = buildChunkStringToSign(dataToChunk);
        String chunkSignature = computeChunkSignature(chunkStringToSign);
        return assembleSignedChunk(dataToChunk, chunkSignature);
    }

    private byte[] prepareDataToChunk(int userDataLen, byte[] userData) {
        if (userDataLen == 0) {
            return FINAL_CHUNK;
        }
        if (userDataLen < userData.length) {
            byte[] dataToChunk = new byte[userDataLen];
            System.arraycopy(userData, 0, dataToChunk, 0, userDataLen);
            return dataToChunk;
        }
        return userData;
    }

    private String buildChunkStringToSign(byte[] dataToChunk) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(CHUNK_STRING_TO_SIGN_PREFIX).append("\n")
              .append(timestamp).append("\n")
              .append(scope).append("\n")
              .append(lastComputedSignature).append("\n")
              .append(hex(hash(""))).append("\n")
              .append(hex(hash(dataToChunk)));
        return buffer.toString();
    }

    private String computeChunkSignature(String chunkStringToSign) {
        try {
            String chunkSignature = hex(readBytes(toInputStream(chunkStringToSign), hmacSHA256));
            lastComputedSignature = chunkSignature;
            return chunkSignature;
        } catch (IOException e) {
            throw new HttpException("hmac sha256 chunked signature error", e);
        }
    }

    private byte[] assembleSignedChunk(byte[] dataToChunk, String chunkSignature) {
        StringBuilder chunkHeader = new StringBuilder();
        chunkHeader.append(Integer.toHexString(dataToChunk.length))
                   .append(CHUNK_SIGNATURE_HEADER).append(chunkSignature)
                   .append(CLRF);

        byte[] header = chunkHeader.toString().getBytes(UTF_8);
        byte[] signedChunk = new byte[header.length + dataToChunk.length + TRAILER.length];
        System.arraycopy(header, 0, signedChunk, 0, header.length);
        System.arraycopy(dataToChunk, 0, signedChunk, header.length, dataToChunk.length);
        System.arraycopy(TRAILER, 0, signedChunk, header.length + dataToChunk.length, TRAILER.length);
        return signedChunk;
    }

    @Override
    public void release() {
        this.payload.release();
    }

    @Override
    public boolean isRepeatable() {
        return this.payload.isRepeatable();
    }

    @Override
    public InputStream openStream() throws IOException {
        return new SequenceInputStream(new ChunkedInputStreamEnumeration(this.payload.openStream(), chunkedBlockSize));
    }

    @SuppressModernizer
    private class ChunkedInputStreamEnumeration implements Enumeration<InputStream> {
        private final InputStream inputStream;
        private boolean lastChunked;
        private byte[] buffer;

        ChunkedInputStreamEnumeration(InputStream inputStream, int chunkedBlockSize) {
            this.inputStream = new BufferedInputStream(inputStream, chunkedBlockSize);
            this.buffer = new byte[chunkedBlockSize];
            this.lastChunked = false;
        }

        @Override
        public boolean hasMoreElements() {
            return !lastChunked;
        }

        @Override
        public InputStream nextElement() {
            int bytesRead;
            try {
                bytesRead = ByteStreams.read(inputStream, buffer, 0, buffer.length);
            } catch (IOException e) {
                throw new ChunkedUploadException("read from input stream error", e);
            }

            byte[] chunk;
            if (bytesRead > 0) {
                chunk = constructSignedChunk(bytesRead, buffer);
            } else {
                chunk = constructSignedChunk(0, buffer);
                lastChunked = true;
            }
            return new ByteArrayInputStream(chunk);
        }
    }
}
