-- Script to fix Role enum values in database
-- Convert all "Driver" to "DRIVER" for consistency

UPDATE Users 
SET role = 'DRIVER' 
WHERE role = 'Driver';

UPDATE Users 
SET role = 'ADMIN' 
WHERE role = 'Admin';

UPDATE Users 
SET role = 'STAFF' 
WHERE role = 'Staff';


