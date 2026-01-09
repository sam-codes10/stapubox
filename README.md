# Stapubox Assignment

## Tech stack
- Java springboot
- My Sql db

## How to run?
- in root dircectory, open terminal and please run ```chmod +x wait-for-mysql.sh```
- then run, ```docker compose down -v``` to stop all containers, networks if any
- then run, ```docker compose up --build``` to build/re-build the image
- open browser for swagger docs -- ```http://localhost:8080/swagger-ui/index.html#/``` **note - use postman if header is required and use Bearer**

## User-flow
- a min-auth , signup and login is required to use secured endpoints. **make sure to provide role as ```ADMIN``` OR ```USER``` ONLY at the time of signup**
- endpoint containing admin can be accessed by ```ADMIN```

## API Endpoints
- # Auth
  - ```/api/min-auth/sign-up``` - for signup, please use role as ```ADMIN``` OR ```ROLE``` and shall get authToken.
  - ```/api/min-auth/login``` - provide email and password and shall get authToken.
    
- # Venue
  - ```/api/venue``` - get all venues, no token required
  - ```/api/venue/available``` get all available venues according to sportId and slot provided, no token required
  - ```/api/venue/add/admin``` adding venue and slot.  sample payload```{"venueName":"string","venueLocation":"string","sportId":0,"slotStartTime":0,"slotEndTime":0}```admin token is required
  - ```/api/venue/delete/admin``` delete venue by venueID. admin token is required

- # Booking
  - ```/api/booking/user/book``` booking of slot is done. Token of user or admin is required.
  - ```/api/booking/user/cancel``` cancel of slot can be done. Token of user or admin is required. 
