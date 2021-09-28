## E-commerce

# :page_facing_up: Description

An small web service in Java using Spring Boot. 

API support:
  - Create a new product
  - Update a product
  - Get a list of all products
  - Placing an order
  - Retrieving all order within a given time period
 
# :computer: Run 

Run local ./setup.sh script

After, you could visit API documentation at <http://localhost:8080/public/swagger-ui.html>

# :bulb: Next step

  - Use OAuth 2.0 with Bearer tokens for authentication
  - Use login credentials for authentication
    - A customer user must supply a username and password in the body of the request:
        ``` 
          POST => https://example.com/login/ 
        
          BODY => {'username': 'test@example.com', 'password': 'Pasw0rd_15'} 
        ```        
        
        - The response will include a JWT access and refresh token:     
        
        ```  
          HTTP status code 201 (Created)
            {
              'access_token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlRlc3QgRXhhbXBsZSIsImlhdCI6MTUxNjIzOTAyMn0.PPl6Br6Ddp7IXsdMM33BEUTCdRAOrRQCYzCfp-VYFQ8'
              
              'refresh_token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IlRlc3QgRXhhbXBsZSIsImlhdCI6MTYxNjUzOTAyMn0.d3d8uahWM8IFODQoeb-_mviJC3hxCxNDACMqQKPZZ8s'
            } 
        ```
    - For admin user(internal company user) it will be used multi-factor authentication, so the admin must supply a username, password, and passcode in the body of the request
    
  - Each protected endpoint must have an authorization header with token that the user received after successful login
      
      ``` 
        Authorization: Bearer <access_token>
      ```
      
  -  As each token expires automatically after a predetermined period (access token expires before refresh token), 
  so to get a new access token user can send a POST request to API /refresh endpoint and include the access token in the authorization header.
     ``` 
        POST => https://example.com/refresh/ 
        
        Authorization: Bearer <old_access_token>
      ```  
      
      - The response will include an JWT access and refresh token 
    
  
