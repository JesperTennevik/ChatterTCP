# ChatterTCP
ChatterTCP is a group chat application built with server-client architecture using Java and Swing.

# Features
  - Custom display name.
  - List of active users.
  - Automatic synchronization of active users.
  - Supports multiple instances on the same machine.
  - WAN and LAN usages.

# Structure
## Client
The Client package handles the client networking and UI.
Client.java is the entry point.

## Server
The server package handles all server-side code. <br>
`ServerListener.java` the actual server spinning up new `Server.java` threads for each connection who then handles the traffic per user.

## Shared
This package is used to structure the data packets sent between the clients and server.

# Issues
No known issues.

