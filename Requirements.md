1. Functional requirements
ID	Requirement	Description
FR1	Vehicle entry	Register an arriving vehicle
FR2	Spot allocation	Allocate a compatible vacant spot
FR3	Ticket generation	Generate a unique parking ticket
FR4	Multi-floor	Support multiple parking floors
FR5	Vehicle compatibility	Support bike, car, and truck
FR6	Vehicle exit	Validate ticket and release spot
FR7	Fee calculation	Calculate charges based on duration
FR8	Payment	Accept cash, card, or UPI
FR9	Availability	Track available spots per floor
FR10	Allocation strategy	Support nearest, first-available, and best-fit

2. Non-functional requirements
1. Thread-safe spot allocation.
2. High availability and fault tolerance.
3. Extensible pricing strategies.
4. Extensible vehicle and spot types.
5. Low coupling and high cohesion.
6. Idempotent payment and exit processing.
7. Auditability of tickets and payments.
8. Fast spot lookup, preferably O(1) or O(log N).

Vehicle types
Vehicle	Compatible spot
BIKE	BIKE, COMPACT, LARGE
CAR	COMPACT, LARGE
TRUCK	LARGE

This is an example compatibility policy. Actual business rules can be changed independently.
Parking spot types
- BIKE: For two-wheelers.
- COMPACT: For cars.
- LARGE: For trucks and larger vehicles.
A large spot may accommodate smaller vehicles if the parking policy permits it.

Design patterns
Use the following patterns to keep the system extensible.
Pattern	Implementation	Purpose
Strategy	SpotAllocationStrategy	Change allocation algorithm
Strategy	FeeCalculationStrategy	Change pricing rules
Factory	VehicleFactory	Create vehicle objects
Factory	ParkingSpotFactory	Create spot objects
Singleton	ParkingLot (optional)	Single logical parking-lot instance
State	Ticket lifecycle	Control ticket transitions
Repository	TicketRepository	Abstract persistence
Observer / Events	ParkingEventPublisher	Publish occupancy and payment events

Important edge cases
Scenario	Expected behaviour
Parking lot full	Reject entry gracefully
Duplicate vehicle entry	Check for existing active ticket
Payment failure	Keep ticket open and spot occupied
Customer scans ticket twice	Idempotent exit processing
Lost ticket	Apply configurable lost-ticket policy
Spot maintenance	Mark OUT_OF_SERVICE
Power failure	Recover state from database
Application restart	Restore active tickets and occupancy
Payment gateway timeout	Reconcile using gateway transaction reference
Vehicle mismatch	Flag for manual verification
Vehicle overstays	Apply additional pricing policy
Two simultaneous entries	Allocate distinct spots

REST API design
Assume Spring Boot REST APIs.
Method	Endpoint	Purpose
POST	/api/v1/vehicles/entry	Vehicle entry and ticket issue
GET	/api/v1/tickets/{ticketId}	Retrieve ticket
GET	/api/v1/parking/availability	Available spot count
GET	/api/v1/parking/floors	List floors
GET	/api/v1/parking/floors/{floorId}/spots	Get floor spots
POST	/api/v1/vehicles/exit/quote	Calculate exit fee
POST	/api/v1/payments	Process payment
POST	/api/v1/vehicles/exit/complete	Complete vehicle exit

