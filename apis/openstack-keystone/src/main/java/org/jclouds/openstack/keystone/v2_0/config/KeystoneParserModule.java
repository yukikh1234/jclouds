
package org.jclouds.openstack.keystone.v2_0.config;

import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.Set;

import org.jclouds.json.config.GsonModule;
import org.jclouds.json.config.GsonModule.DateAdapter;
import org.jclouds.json.internal.NullFilteringTypeAdapterFactories.SetTypeAdapter;
import org.jclouds.json.internal.NullFilteringTypeAdapterFactories.SetTypeAdapterFactory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.inject.AbstractModule;

public class KeystoneParserModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(DateAdapter.class).to(GsonModule.Iso8601DateAdapter.class);
      bind(SetTypeAdapterFactory.class).to(ValuesSetTypeAdapterFactory.class);
   }

   public static class ValuesSetTypeAdapterFactory extends SetTypeAdapterFactory {

      @Override
      @SuppressWarnings("unchecked")
      protected <E, I> TypeAdapter<I> newAdapter(TypeAdapter<E> elementAdapter) {
         return (TypeAdapter<I>) new Adapter<>(elementAdapter);
      }

      public static final class Adapter<E> extends TypeAdapter<Set<E>> {

         private final SetTypeAdapter<E> delegate;

         public Adapter(TypeAdapter<E> elementAdapter) {
            this.delegate = new SetTypeAdapter<>(elementAdapter);
            nullSafe();
         }

         public void write(JsonWriter out, Set<E> value) throws IOException {
            delegate.write(out, value);
         }

         @Override
         public Set<E> read(JsonReader in) throws IOException {
            return in.peek() == JsonToken.BEGIN_OBJECT ? readObject(in) : delegate.read(in);
         }

         private Set<E> readObject(JsonReader in) throws IOException {
            Builder<E> builder = ImmutableSet.builder();
            boolean foundValues = false;
            in.beginObject();
            while (in.hasNext()) {
               String name = in.nextName();
               if (Objects.equal("values", name)) {
                  foundValues = true;
                  builder.addAll(delegate.read(in));
               } else {
                  in.skipValue();
               }
            }
            checkState(foundValues, "Expected BEGIN_ARRAY or the object to contain an array called 'values'");
            in.endObject();
            return builder.build();
         }
      }
   }
}
