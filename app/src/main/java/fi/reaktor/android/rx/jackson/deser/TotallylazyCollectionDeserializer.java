package fi.reaktor.android.rx.jackson.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;

public abstract class TotallylazyCollectionDeserializer<T>
        extends StdDeserializer<T>
        implements ContextualDeserializer {
    private static final long serialVersionUID = 1L;

    protected final CollectionType _containerType;

    /**
     * Deserializer used for values contained in collection being deserialized;
     * either assigned on constructor, or during resolve().
     */
    protected final JsonDeserializer<?> _valueDeserializer;

    /**
     * If value instances have polymorphic type information, this
     * is the type deserializer that can deserialize required type
     * information
     */
    protected final TypeDeserializer _typeDeserializerForValue;

    protected TotallylazyCollectionDeserializer(CollectionType type,
                                                TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
        super(type);
        _containerType = type;
        _typeDeserializerForValue = typeDeser;
        _valueDeserializer = deser;
    }

    /**
     * Overridable fluent factory method used for creating contextual
     * instances.
     */
    public abstract TotallylazyCollectionDeserializer<T> withResolved(
            TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser);
    
    /*
    /**********************************************************
    /* Validation, post-processing
    /**********************************************************
     */

    /**
     * Method called to finalize setup of this deserializer,
     * after deserializer itself has been registered. This
     * is needed to handle recursive and transitive dependencies.
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt,
                                                BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> deser = _valueDeserializer;
        TypeDeserializer typeDeser = _typeDeserializerForValue;
        if (deser == null) {
            deser = ctxt.findContextualValueDeserializer(_containerType.getContentType(), property);
        }
        if (typeDeser != null) {
            typeDeser = typeDeser.forProperty(property);
        }
        if (deser == _valueDeserializer && typeDeser == _typeDeserializerForValue) {
            return this;
        }
        return withResolved(typeDeser, deser);
    }

    /*
    /**********************************************************
    /* Deserialization interface
    /**********************************************************
     */

    /**
     * Base implementation that does not assume specific type
     * inclusion mechanism. Sub-classes are expected to override
     * this method if they are to handle type information.
     */
    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
                                      TypeDeserializer typeDeserializer)
            throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        // Should usually point to START_ARRAY
        if (jp.isExpectedStartArrayToken()) {
            return _deserializeContents(jp, ctxt);
        }
        // But may support implicit arrays from single values?
        if (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            return _deserializeFromSingleValue(jp, ctxt);
        }
        throw ctxt.mappingException(_containerType.getRawClass());
    }

    /*
    /**********************************************************************
    /* Abstract methods for impl classes
    /**********************************************************************
     */

    protected abstract T _deserializeContents(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException;

    /**
     * Method used to support implicit coercion from a single non-array value
     * into single-element collection.
     *
     * @since 2.3
     */
    protected abstract T _deserializeFromSingleValue(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException;
}