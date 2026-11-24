@Service
public class ParkingService {

    private final List<ParkingFloor> floors;

    private final SpotAllocationStrategy allocationStrategy;

    private final TicketRepository ticketRepository;

    public ParkingService(
            List<ParkingFloor> floors,
            SpotAllocationStrategy allocationStrategy,
            TicketRepository ticketRepository) {

        this.floors = floors;
        this.allocationStrategy = allocationStrategy;
        this.ticketRepository = ticketRepository;
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {

        ParkingSpot spot = allocationStrategy
                .allocate(vehicle, floors)
                .orElseThrow(() ->
                    new NoParkingSpotAvailableException(
                        "No compatible parking spot"));

        spot.occupy(vehicle);

        ParkingTicket ticket = new ParkingTicket(
                UUID.randomUUID().toString(),
                vehicle.getRegistrationNumber(),
                spot.getSpotId(),
                spot.getFloorId()
        );

        return ticketRepository.save(ticket);
    }

    public BigDecimal calculateFee(
            ParkingTicket ticket,
            Instant exitTime,
            FeeCalculationStrategy feeStrategy) {

        return feeStrategy.calculateFee(
                ticket, exitTime);
    }
}