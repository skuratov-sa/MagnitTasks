package com.skuratov.springeactivewmq.repo;

import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;


@Repository
public class MsgRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${query.insert.msg_body}")
    private String insertBodyTable;

    @Value("${query.insert.msg_headers}")
    private String insertHeaderTable;

    @Value("${query.select.msg_body}")
    private String selectBodyTable;

    @Value("${query.select.msg_headers}")
    private String selectHeaderTable;

    public MsgRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SneakyThrows
    @Transactional
    public void addMessage(ActiveMQTextMessage message) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(createStatementByBodyTable(message), keyHolder);
        jdbcTemplate.update(insertBodyTable, message.getText(), keyHolder.getKey());

    }

    @Scheduled(initialDelay = 10_000, fixedRate = 10_000)
    public void printAll() {

        System.out.println("table_1:");
        System.out.println("\t| id     |               MESSAGE               |header_id|");
        System.out.println("\t-------------------------------------------------------");
        for (var map : jdbcTemplate.queryForList(selectBodyTable)) {
            System.out.printf("\t| %s \t| %s \t| %s \t|\n",
                    map.get("ID"),
                    map.get("MESSAGE"),
                    map.get("msg_header_id")
            );
        }

        System.out.println("\ntable_2:");
        System.out.println("\t| id    |   user_id    |                  msg_id                           |    group_id    |");
        System.out.println("\t--------------------------------------------------------------------------------------------");
        for (var map : jdbcTemplate.queryForList(selectHeaderTable)) {
            System.out.printf("\t| %s \t| %s \t| %s \t| %s \t|\n",
                    map.get("ID"),
                    map.get("user_id"),
                    map.get("msg_id"),
                    map.get("group_id")
            );
        }
    }


    private PreparedStatementCreator createStatementByBodyTable(ActiveMQTextMessage message) {
        return conn -> {
            var ps = conn.prepareStatement(insertHeaderTable, RETURN_GENERATED_KEYS);
            ps.setString(1, message.getUserID());
            ps.setString(2, message.getMessageId().toProducerKey());
            ps.setString(3, message.getGroupID());
            return ps;
        };
    }
}
