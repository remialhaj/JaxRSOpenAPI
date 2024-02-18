USER:
CHANGERID : mettre l'id générer voir dans la base de donnée
curl -X POST -H "Content-Type: application/json" -d '{"username":"JohnDoe","email":"john.doe@example.com","password":"password123"}' http://localhost:8080/user/
curl -X POST -H "Content-Type: application/json" -d '{"username":"RickyBobby","email":"rick@example.com","password":"password123"}' http://localhost:8080/user/
curl -X GET http://localhost:8080/user/CHANGERIDUSER
curl -X GET http://localhost:8080/user/
curl -X PUT -H "Content-Type: application/json" -d '{"username":"NewUsername","email":"new.email@example.com","password":"newpassword"}' http://localhost:8080/user/CHANGERIDUSER
curl -X DELETE http://localhost:8080/user/CHANGERIDUSER

PET:
CHANGERID : mettre l'id générer voir dans la base de donnée
curl -X POST -H "Content-Type: application/json" -d '{"name":"Remi"}' http://localhost:8080/pet
curl -X POST -H "Content-Type: application/json" -d '{"name":"Rex"}' http://localhost:8080/pet
curl -X GET http://localhost:8080/pet/CHANGERIDUSER
curl -X GET http://localhost:8080/pet/
curl -X PUT -H "Content-Type: application/json" -d '{"name":"NewName"}' http://localhost:8080/pet/CHANGERIDUSER
curl -X DELETE http://localhost:8080/pet/CHANGERIDUSER

TAG:
curl -X POST -H "Content-Type: application/json" -d '{"name":"Bug"}' http://localhost:8080/user/tags

TICKET:
curl -X POST http://localhost:8080/user/CHANGERIDUSER/tickets -H 'Content-Type: application/json' -d '{"title":"Issue 1","description":"Description of Issue 1","createdBy":{"id":CHANGERIDUSER},"assignedTo":{"id":CHANGERIDUSER},"tags":[{"id":CHANGERIDTAG}]}'

COMMENT:
curl -X POST http://localhost:8080/user/CHANGERIDUSER/tickets/CHANGERIDTICKET/comments -H 'Content-Type: application/json' -d '{"content":"This is a comment about issue 1","createdBy":{"id":CHANGERIDUSER}}'
