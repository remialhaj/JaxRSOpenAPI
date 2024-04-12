TP5
Partie 2:
Question 6,7,8 :
Ajout des classes DAO et classe domain
Création de la UserResource et SwaggerResource
Ajout dépandance dans le pom.xml swagger
Dossier swagger ajouté dans webapp
ajout des @XmlRootElement pour les entités pour afficher en xml
ajout des @XmlTransient pour les entités pour ne pas afficher en xml

Ajout des GET, POST, PUT, DELETE pour les entités, PATH, CONSUMES


USER:
CHANGERID : mettre l'id générer voir dans la base de donnée
curl -X POST -H "Content-Type: application/json" -d '{"username":"JohnDoe","email":"john.doe@example.com","password":"password123"}' http://localhost:8081/user/register
curl -X POST -H "Content-Type: application/json" -d '{"username":"RickyBobby","email":"rick@example.com","password":"password123"}' http://localhost:8080/user/register
curl -X GET http://localhost:8080/user/CHANGERIDUSER
curl -X GET http://localhost:8080/user/
curl -X PUT -H "Content-Type: application/json" -d '{"username":"NewUsername","email":"new.email@example.com","password":"newpassword"}' http://localhost:8080/user/CHANGERIDUSER
curl -X DELETE http://localhost:8080/user/CHANGERIDUSER

TAG:
curl -X POST -H "Content-Type: application/json" -d '{"name":"Bug"}' http://localhost:8081/user/tags

TICKET:
curl -X POST http://localhost:8080/user/john.doe@example.com/tickets -H 'Content-Type: application/json' -d '{"title":"Issue 2","description":"Description of Issue 2","assignedTo":{"id":253},"tags":[{"id":2}]}'

COMMENT:
curl -X POST http://localhost:8080/user/a@gmail.com/tickets/1/comments -H 'Content-Type: application/json' -d '{"content":"This is a comment about issue 1","createdBy":{"id":1}}'


curl -X POST -H "Content-Type: application/json" -d '{"username":"RRR","email":"RRRRR.doe@example.com","password":"password123"}' http://localhost:8080/admin