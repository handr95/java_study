package study.mapper;

import java.util.List;

import study.model.Vehicle;

public interface VehicleMapper {
    void insert(Vehicle vehicle);
    void update(Vehicle vehicle);
    void delete(Vehicle vehicle);
    Vehicle findByVehicleNo(String vehicleNo);
    List<Vehicle> findAll();
}
