package com.otbs.service;

import com.otbs.model.Connection;
import com.otbs.model.ConnectionLog;
import com.otbs.repository.ConnectionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConnectionLogService {

    @Autowired
    private ConnectionLogRepository connectionLogRepository;

    // Log the connection changes
    public void logConnectionChange(Connection connection, String Status) {
    	
    	calculateProcessingFee(connection);
    	
        ConnectionLog connectionLog = new ConnectionLog();
        connectionLog.setConnectionObj(connection);
        connectionLog.setConnectionType(connection.getConnectionType());
        connectionLog.setNetworkType(connection.getNetworkType());
        connectionLog.setStatus(Status);
        connectionLog.setChangedate(LocalDate.now());

        connectionLogRepository.save(connectionLog);
    }

    // Fetch all logs
    public List<ConnectionLog> getAllLogs() {
        return connectionLogRepository.findAll();
    }

    // Fetch logs for a specific connection
    public List<ConnectionLog> getLogsByConnectionId(int connectionId) {
        return connectionLogRepository.findAll()
                .stream()
                .filter(log -> log.getConnectionObj().getConnectionId() == connectionId)
                .toList();
    }

    // Calculate processing fee
    public void calculateProcessingFee(Connection connection) {
        double processingFee = 0.0;

        String connectionType = connection.getConnectionType().toUpperCase();
        String networkType = connection.getNetworkType().toUpperCase();

        switch (connectionType) {
            case "PREPAID":
                processingFee = networkType.equals("4G") ? 200.0 : 250.0;
                break;

            case "POSTPAID":
                processingFee = networkType.equals("4G") ? 300.0 : 350.0;
                break;

            case "BROADBAND":
                processingFee = networkType.equals("4G") ? 400.0 : 450.0;
                break;

            default:
                throw new IllegalArgumentException("Invalid connection type: " + connectionType);
        }

        connection.setProcessingFee(processingFee);
    }
}
