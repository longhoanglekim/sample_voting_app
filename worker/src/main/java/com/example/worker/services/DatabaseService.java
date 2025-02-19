package com.example.worker.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DatabaseService {
    private final DataSource dataSource;
    @Autowired
    private RedisService redisService;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveOrUpdateVote(String voterId, String vote) {
        String query = "INSERT INTO votes (voter_id, choice) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE vote = VALUES(choice)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, voterId);
            statement.setString(2, vote);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ MySQL DB error: " + e.getMessage());
        }
    }

    public Map<String, Integer> getCountVotes() {
        Map<String, Integer> votes = new HashMap<>();
        String query = "SELECT distinct choice FROM votes";
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet resultSet = statement.getResultSet();
            // if not empty
            if (resultSet.next()) {
                while (resultSet.next()) {
                    if (votes.containsKey(resultSet.getString("choice"))) {
                        votes.put(resultSet.getString("choice"), votes.get(resultSet.getString("choice")) + 1);
                    } else {
                        votes.put(resultSet.getString("choice"), 1);
                    }
                }
            } else {
                List<String> choices = redisService.popVotes();
                for (String choice : choices) {
                    if (votes.containsKey(choice)) {
                        votes.put(choice, votes.get(choice) + 1);
                    } else {
                        votes.put(choice, 1);
                    }
                }
            }
            return votes;
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public void keepAlive() {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                log.debug("Query result: {}", rs.getInt(1));  // Log kết quả trả về từ query
            }
        } catch (SQLException e) {
            System.err.println("⚠️ MySQL DB connection lost, reconnecting...");
        }
    }
}
