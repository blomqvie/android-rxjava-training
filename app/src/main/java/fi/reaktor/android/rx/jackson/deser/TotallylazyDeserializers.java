package fi.reaktor.android.rx.jackson.deser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;

/**
 * Custom deserializers module offers.
 */
public class TotallylazyDeserializers
        extends Deserializers.Base {
    /**
     * We have plenty of collection types to support...
     */
    @Override
    public JsonDeserializer<?> findCollectionDeserializer(CollectionType type,
                                                          DeserializationConfig config, BeanDescription beanDesc,
                                                          TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException {
        return new SequenceDeserializer(type,
                elementTypeDeserializer, elementDeserializer);
    }

    @Override
    public JsonDeserializer<?> findMapDeserializer(MapType type,
                                                   DeserializationConfig config, BeanDescription beanDesc,
                                                   KeyDeserializer keyDeserializer,
                                                   TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException {
        return null;
    }

    @Override
    public JsonDeserializer<?> findMapLikeDeserializer(MapLikeType type,
                                                       DeserializationConfig config, BeanDescription beanDesc,
                                                       KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer,
                                                       JsonDeserializer<?> elementDeserializer)
            throws JsonMappingException {
        return null;
    }

    @Override
    public JsonDeserializer<?> findBeanDeserializer(final JavaType type, DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        return super.findBeanDeserializer(type, config, beanDesc);
    }
}