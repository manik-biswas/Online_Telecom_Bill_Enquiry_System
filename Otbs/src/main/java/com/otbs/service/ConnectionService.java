package com.otbs.service;

import com.otbs.exception.InvalidEntityException;
import com.otbs.model.Connection;
import com.otbs.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;
    

    @Autowired
    private ConnectionLogService connectionLogService; 

    public ConnectionService(ConnectionRepository connectionRepository) {
    	this.connectionRepository = connectionRepository;
        
    }

    // Activate a new connection
    public Connection activateConnection(Connection connection) throws InvalidEntityException {
        if (connection == null || connection.getConnectionType() == null || connection.getNetworkType() == null) {
            throw new InvalidEntityException("Invalid connection details provided");
        }

        calculateProcessingFee(connection);
        connection.setStatus("Active");
        Connection savedConnection = connectionRepository.save(connection);

        // Log the activation
        connectionLogService.logConnectionChange(savedConnection, "ACTIVATE");

        return savedConnection;
    }

    // Upgrade an existing connection
    public Connection upgradePlan(Connection connection, int connectionId) throws InvalidEntityException {
        Optional<Connection> existingConnectionOpt = connectionRepository.findById(connectionId);

        if (!existingConnectionOpt.isPresent()) {
            throw new InvalidEntityException("Connection with ID " + connectionId + " not found");
        }

        Connection existingConnection = existingConnectionOpt.get();
        existingConnection.setConnectionType(connection.getConnectionType());
        existingConnection.setNetworkType(connection.getNetworkType());
        existingConnection.setProcessingFee(connection.getProcessingFee());
        existingConnection.setStatus("Upgraded");

        Connection updatedConnection = connectionRepository.save(existingConnection);

        // Log the upgrade
        connectionLogService.logConnectionChange(updatedConnection, "UPGRADE");

        return updatedConnection;
    }

    // Terminate an existing connection
    public boolean terminateConnection(int connectionId) throws InvalidEntityException {
        Optional<Connection> connectionOpt = connectionRepository.findById(connectionId);

        if (!connectionOpt.isPresent()) {
            throw new InvalidEntityException("Connection with ID " + connectionId + " not found");
        }

        Connection connectionToTerminate = connectionOpt.get();
        connectionToTerminate.setStatus("Terminated");
        connectionRepository.save(connectionToTerminate);

        // Log the termination
        connectionLogService.logConnectionChange(connectionToTerminate, "TERMINATE");

        return true;
    }

    // Fetch all connections
    public List<Connection> getAllConnections() throws InvalidEntityException {
        List<Connection> connections = connectionRepository.findAll();

        if (connections.isEmpty()) {
            throw new InvalidEntityException("No connections found");
        }

        return connections.stream().collect(Collectors.toList());
    }

    // Fetch connection by ID
    public Connection getByConnectionId(int connectionId) throws InvalidEntityException {
        return connectionRepository.findById(connectionId)
                .orElseThrow(() -> new InvalidEntityException("Connection with ID " + connectionId + " not found"));
    }
    
    
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
   
    
    public List<Connection> getConnectionsNearingExpiry() {
        List<Connection> connections = connectionRepository.findAll();

        return connections.stream()
                .filter(connection -> {
                    LocalDate expiryDate = connection.getActivationdate().plusDays(connection.getPlan().getNumberOfDay());
                    LocalDate today = LocalDate.now();
                    return expiryDate.isAfter(today) && expiryDate.isBefore(today.plusDays(5));
                })
                .collect(Collectors.toList());
    }

    
//  view all connection related to a perticular customer
    public List<Connection> getConnectionsByCustomerId(int customerId) {
        return connectionRepository.findByCustomerObjCustomerId(customerId);
    }
    
    
}
