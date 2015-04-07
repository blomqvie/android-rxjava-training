package fi.reaktor.android.rx.jackson.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.io.IOException;

public class SequenceDeserializer<T extends Sequence<Object>>
        extends TotallylazyCollectionDeserializer<T> {
    private static final long serialVersionUID = 1L;

    public SequenceDeserializer(CollectionType type,
                                TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
        super(type, typeDeser, deser);
    }

    @Override
    public SequenceDeserializer withResolved(TypeDeserializer typeDeser,
                                             JsonDeserializer<?> valueDeser) {
        return new SequenceDeserializer(_containerType, typeDeser,
                valueDeser);
    }

    @Override
    protected T _deserializeContents(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonDeserializer<?> valueDes = _valueDeserializer;
        JsonToken t;
        final TypeDeserializer typeDeser = _typeDeserializerForValue;
        // No way to pass actual type parameter; but does not matter, just
        // compiler-time fluff:
        Sequence<Object> sequence = Sequences.sequence();

        while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
            Object value;

            if (t == JsonToken.VALUE_NULL) {
                value = null;
            } else if (typeDeser == null) {
                value = valueDes.deserialize(jp, ctxt);
            } else {
                value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
            }
            sequence = sequence.append(value);
        }
        // No class outside of the package will be able to subclass us,
        // and we provide the proper builder for the subclasses we implement.
        @SuppressWarnings("unchecked")
        T collection = (T) sequence;
        return collection;
    }

    @Override
    protected T _deserializeFromSingleValue(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonDeserializer<?> valueDes = _valueDeserializer;
        final TypeDeserializer typeDeser = _typeDeserializerForValue;
        JsonToken t = jp.getCurrentToken();

        Object value;

        if (t == JsonToken.VALUE_NULL) {
            value = null;
        } else if (typeDeser == null) {
            value = valueDes.deserialize(jp, ctxt);
        } else {
            value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
        }
        @SuppressWarnings("unchecked")
        T result = (T) Sequences.sequence(value);
        return result;
    }
}