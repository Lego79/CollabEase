package com.master.side.infrastructure.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // 1) 전역 기본 포맷 지정 (String 기반)
            builder.simpleDateFormat("yyyy.MM.dd HH:mm:ss");
            // 2) LocalDateTime 전용 Serializer 등록
            builder.serializers(
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"))
            );
            // 필요하다면 Deserializer도 등록 가능
            // builder.deserializers(
            //    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"))
            // );
        };
    }
}
