# Hotel Booking Module Analysis

The Hotel Booking module allows registered users to search, compare, and book hotels from multiple external providers through a single application.

Instead of connecting directly to a specific provider, the application uses a **Provider Aggregator** that communicates with multiple hotel providers, collects hotel data, normalizes the responses, and returns a unified result to the user.

The booking process includes:

- Hotel search
- Room availability verification
- User authentication
- Booking creation
- Payment processing
- Booking confirmation
- Email notification

## FlowChart
![Flowchart]([images/bookingHotel-flowchart.png])

# Booking History
The Booking History module allows users to retrieve all previous bookings stored in the local database. Users can filter their bookings, view booking details, and optionally retrieve the latest booking status from the external provider.

