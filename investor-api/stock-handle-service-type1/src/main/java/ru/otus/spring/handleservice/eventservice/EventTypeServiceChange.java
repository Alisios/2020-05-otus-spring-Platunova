package ru.otus.spring.handleservice.eventservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.configuration.MessageProperties;
import ru.otus.spring.handleservice.models.StockInfoFull;
import ru.otus.spring.handleservice.models.StockInfoMapper;
import ru.otus.spring.handleservice.models.StockInfoRes;

@Service("${event-type-handler.eventType.event_1}")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EventTypeServiceChange implements EventTypeService {

    StockInfoMapper mapper;
    MessageProperties properties;

    @Override
    public StockInfoRes checkEvent(StockInfoFull info) {
        if (info.getChange() >= 0) {
            StockInfoRes res = mapper.map(info);
            res.setTypeEvent(properties.getChange().get("name"));
            return res;
        } else return null;
    }

    public String createMessage(StockInfoRes info) {
        return properties.getChange().get("message") + " "
                + info.getCompanyName() + " "
                + properties.getChange().get("middle")
                + " " + info.getChange() + "%!";
    }
}
