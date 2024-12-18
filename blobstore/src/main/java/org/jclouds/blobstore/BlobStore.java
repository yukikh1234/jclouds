/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.blobstore;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobAccess;
import org.jclouds.blobstore.domain.BlobBuilder;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.ContainerAccess;
import org.jclouds.blobstore.domain.MultipartPart;
import org.jclouds.blobstore.domain.MultipartUpload;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.CopyOptions;
import org.jclouds.blobstore.options.CreateContainerOptions;
import org.jclouds.blobstore.options.GetOptions;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.domain.Location;
import org.jclouds.io.Payload;
import org.jclouds.javax.annotation.Nullable;

import com.google.common.annotations.Beta;

/**
 * Synchronous access to a BlobStore such as Amazon S3
 */
public interface BlobStore {
   /**
    * @return a reference to the context that created this BlobStore.
    */
   BlobStoreContext getContext();

   /**
    *
    * @return builder for creating new {@link Blob}s
    */
   BlobBuilder blobBuilder(String name);

   /**
    * The get locations command returns all the valid locations for containers. A location has a
    * scope, which is typically region or zone. A region is a general area, like eu-west, where a
    * zone is similar to a datacenter. If a location has a parent, that implies it is within that
    * location. For example a location can be a rack, whose parent is likely to be a zone.
    */
   Set<? extends Location> listAssignableLocations();

   /**
    * Lists all root-level resources available to the identity.
    */
   PageSet<? extends StorageMetadata> list();

   /**
    * determines if a service-level container exists
    */
   boolean containerExists(String container);

   /**
    * Creates a namespace for your blobs
    * <p/>
    *
    * A container is a namespace for your objects. Depending on the service, the scope can be
    * global, identity, or sub-identity scoped. For example, in Amazon S3, containers are called
    * buckets, and they must be uniquely named such that no-one else in the world conflicts. In
    * other blobstores, the naming convention of the container is less strict. All blobstores allow
    * you to list your containers and also the contents within them. These contents can either be
    * blobs, folders, or virtual paths.
    *
    * @param location
    *           some blobstores allow you to specify a location, such as US-EAST, for where this
    *           container will exist. null will choose a default location
    * @param container
    *           namespace. Typically constrained to lowercase alpha-numeric and hyphens.
    * @return true if the container was created, false if it already existed.
    */
   boolean createContainerInLocation(@Nullable Location location, String container);

   /**
    *
    * @param options
    *           controls default access control
    * @see #createContainerInLocation(Location,String)
    */
   boolean createContainerInLocation(@Nullable Location location, String container, CreateContainerOptions options);

   @Beta
   ContainerAccess getContainerAccess(String container);

   @Beta
   void setContainerAccess(String container, ContainerAccess access);

   /**
    * Lists all resources in a container non-recursive.
    *
    * @param container
    *           what to list
    * @return a list that may be incomplete, depending on whether PageSet#getNextMarker is set
    */
   PageSet<? extends StorageMetadata> list(String container);

   /**
    * Like {@link #list(String)} except you can control the size, recursion, and context of the list
    * using {@link ListContainerOptions options}
    *
    * @param container
    *           what to list
    * @param options
    *           size, recursion, and context of the list
    * @return a list that may be incomplete, depending on whether PageSet#getNextMarker is set
    */
   PageSet<? extends StorageMetadata> list(String container, ListContainerOptions options);

   /**
    * This will delete the contents of a container at its root path without deleting the container
    *
    * @param container
    *           what to clear
    */
   void clearContainer(String container);

   /**
    * Like {@link #clearContainer(String)} except you can use options to do things like recursive
    * deletes, or clear at a different path than root.
    *
    * @param container
    *           what to clear
    * @param options
    *           recursion and path to clear
    */
   void clearContainer(String container, ListContainerOptions options);

   /**
    * This will delete everything inside a container recursively.
    *
    * @param container
    *           what to delete
    * @param container name of the container to delete
    */
   void deleteContainer(String container);

   /**
    * Deletes a container if it is empty.
    *
    * @param container name of the container to delete
    * @return true if the container was deleted or does not exist
    */
   boolean deleteContainerIfEmpty(String container);

   /**
    * Determines if a directory exists
    *
    * @param container
    *           container where the directory resides
    * @param directory
    *           full path to the directory
    * @deprecated use prefix and delimiter instead
    */
   @Deprecated
   boolean directoryExists(String container, String directory);

   /**
    * Creates a folder or a directory marker depending on the service
    *
    * @param container
    *           container to create the directory in
    * @param directory
    *           full path to the directory
    * @deprecated use prefix and delimiter instead
    */
   @Deprecated
   void createDirectory(String container, String directory);

   /**
    * Deletes a folder or a directory marker depending on the service
    *
    * @param container
    *           container to delete the directory from
    * @param directory
    *           full path to the directory to delete
    * @deprecated use prefix and delimiter instead
    */
   @Deprecated
   void deleteDirectory(String containerName, String name);

   /**
    * Determines if a blob exists
    *
    * @param container
    *           container where the blob resides
    * @param directory
    *           full path to the blob
    */
   boolean blobExists(String container, String name);

   /**
    * Adds a {@code Blob} representing the data at location {@code container/blob.metadata.name}
    *
    * @param container
    *           container to place the blob.
    * @param blob
    *           fully qualified name relative to the container.
    * @param options
    *           byte range or condition options
    * @return etag of the blob you uploaded, possibly null where etags are unsupported
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   String putBlob(String container, Blob blob);

   /**
    * Adds a {@code Blob} representing the data at location {@code container/blob.metadata.name}
    * options using multipart strategies.
    *
    * @param container
    *           container to place the blob.
    * @param blob
    *           fully qualified name relative to the container.
    * @param options
    *           byte range options
    * @return etag of the blob you uploaded, possibly null where etags are unsupported
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   String putBlob(String container, Blob blob, PutOptions options);

   /**
    * Copy blob from one container to another.  Some providers implement this
    * more efficiently than corresponding getBlob and putBlob operations.
    *
    * Note: options are currently ignored
    *
    * @return ETag of new blob
    * @throws ContainerNotFoundException if either container does not exist
    */
   @Beta
   String copyBlob(String fromContainer, String fromName, String toContainer, String toName,
         CopyOptions options);

   /**
    * Retrieves the metadata of a {@code Blob} at location {@code container/name}
    *
    * @param container
    *           container where this exists.
    * @param name
    *           fully qualified name relative to the container.
    * @return null if name isn't present or the blob you intended to receive.
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   @Nullable
   BlobMetadata blobMetadata(String container, String name);

   /**
    * Retrieves a {@code Blob} representing the data at location {@code container/name}
    *
    * @param container
    *           container where this exists.
    * @param name
    *           fully qualified name relative to the container.
    * @return the blob you intended to receive or null, if it doesn't exist.
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   @Nullable
   Blob getBlob(String container, String name);

   /**
    * Retrieves a {@code Blob} representing the data at location {@code container/name}
    *
    * @param container
    *           container where this exists.
    * @param name
    *           fully qualified name relative to the container.
    * @param options
    *           byte range or condition options
    * @return the blob you intended to receive or null, if it doesn't exist.
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   @Nullable
   Blob getBlob(String container, String name, GetOptions options);

   /**
    * Deletes a {@code Blob} representing the data at location {@code container/name}
    *
    * @param container
    *           container where this exists.
    * @param name
    *           fully qualified name relative to the container.
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   void removeBlob(String container, String name);

   /**
    * Deletes multiple {@code Blob}s representing the data at location {@code container/name}
    *
    * @param container
    *           container where this exists.
    * @param names
    *           fully qualified names relative to the container.
    * @throws ContainerNotFoundException
    *            if the container doesn't exist
    */
   void removeBlobs(String container, Iterable<String> names);

   @Beta
   BlobAccess getBlobAccess(String container, String name);

   @Beta
   void setBlobAccess(String container, String name, BlobAccess access);

   /**
    * @return a count of all blobs in the container, excluding directory markers
    */
   long countBlobs(String container);

   /**
    * @return a count of all blobs that are in a listing constrained by the options specified,
    *         excluding directory markers
    */
   long countBlobs(String container, ListContainerOptions options);

   @Beta
   MultipartUpload initiateMultipartUpload(String container, BlobMetadata blob, PutOptions options);

   @Beta
   // TODO: take parts?
   void abortMultipartUpload(MultipartUpload mpu);

   @Beta
   String completeMultipartUpload(MultipartUpload mpu, List<MultipartPart> parts);

   @Beta
   MultipartPart uploadMultipartPart(MultipartUpload mpu, int partNumber, Payload payload);

   @Beta
   List<MultipartPart> listMultipartUpload(MultipartUpload mpu);

   @Beta
   List<MultipartUpload> listMultipartUploads(String container);

   @Beta
   long getMinimumMultipartPartSize();

   @Beta
   long getMaximumMultipartPartSize();

   @Beta
   int getMaximumNumberOfParts();

   @Beta
   void downloadBlob(String container, String name, File destination);

   @Beta
   void downloadBlob(String container, String name, File destination, ExecutorService executor);

   @Beta
   InputStream streamBlob(String container, String name);

   @Beta
   InputStream streamBlob(String container, String name, ExecutorService executor);
}
