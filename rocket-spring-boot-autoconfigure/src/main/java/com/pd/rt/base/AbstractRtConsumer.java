package com.pd.rt.base;

import com.google.gson.Gson;
import com.pd.rt.exception.RtException;
import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.pd.rt.utils.MsgExtConstants.*;

/**
 * @author peramdy on 2018/9/30.
 */
public abstract class AbstractRtConsumer<T> {

    protected static Gson gson = new Gson();

    /**
     * parse message
     *
     * @param msg
     * @return
     */
    protected T parseMsg(MessageExt msg) {
        if (msg == null || msg.getBody() == null) {
            return null;
        }
        final Type type = this.getMessageType();
        if (type instanceof Class) {
            T data = gson.fromJson(new String(msg.getBody()), type);
            return data;
        }
        return null;
    }

    /**
     * parse T class type
     *
     * @return
     */
    protected Type getMessageType() {
        Type superType = this.getClass().getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length != 1) {
                throw new RtException("actual type arguments length must be 1");
            }
            return actualTypeArguments[0];
        } else {
            return Object.class;
        }
    }


    /**
     * parseExtParam
     *
     * @param message
     * @return
     */
        protected Map<String, Object> parseExtParam(MessageExt message) {
        Map<String, Object> extMap = new HashMap<>();
        extMap.put(PROPERTY_TOPIC, message.getTopic());
        extMap.putAll(message.getProperties());
        extMap.put(PROPERTY_EXT_BORN_HOST, message.getBornHost());
        extMap.put(PROPERTY_EXT_BORN_TIMESTAMP, message.getBornTimestamp());
        extMap.put(PROPERTY_EXT_COMMIT_LOG_OFFSET, message.getCommitLogOffset());
        extMap.put(PROPERTY_EXT_MSG_ID, message.getMsgId());
        extMap.put(PROPERTY_EXT_PREPARED_TRANSACTION_OFFSET, message.getPreparedTransactionOffset());
        extMap.put(PROPERTY_EXT_QUEUE_ID, message.getQueueId());
        extMap.put(PROPERTY_EXT_QUEUE_OFFSET, message.getQueueOffset());
        extMap.put(PROPERTY_EXT_RE_CONSUME_TIMES, message.getReconsumeTimes());
        extMap.put(PROPERTY_EXT_STORE_HOST, message.getStoreHost());
        extMap.put(PROPERTY_EXT_STORE_SIZE, message.getStoreSize());
        extMap.put(PROPERTY_EXT_STORE_TIMESTAMP, message.getStoreTimestamp());
        extMap.put(PROPERTY_EXT_SYS_FLAG, message.getSysFlag());
        extMap.put(PROPERTY_EXT_BODY_CRC, message.getBodyCRC());
        return extMap;
    }

}

