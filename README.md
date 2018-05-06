# API

## Générer un jar lançant un main en particulier pour tester
* Ecrire un main avec tout les tests voulu
* Modifier le pom.xml du module maven voulu (ex. utils/pom.xml) et modfier la ligne <mainClass></mainClass> par le nom de la classe contenant le main
* mvn install
* Lancer le jar apparu dans le module à tester