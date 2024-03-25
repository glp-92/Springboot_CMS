# Configuracion de Apache ApiSix junto a Keycloak

KeyCloak funciona como un servidor que gestiona autenticación y autorización fácilmente implementable con soluciones que implementan el protocolo OpenID

En este caso, se puede utilizar en combinación con el balanceador de carga - proxy inverso Apache ApiSix, permitiendo establecer un servidor perimetral con un servicio de autenticación orientado a produccion

## Pasos a seguir

1. `docker compose up` ejecutará todos los servicios establecidos en el fichero `docker-compose.yml`. Por defecto usará una red bridge, necesaria para probar las conexiones desde el PC de desarrollo
2. `http://localhost:9000/admin` para acceder a la consola de KeyCloak, los datos de acceso se han especificado **con fines de desarrollo** en el fichero `docker-compose.yml`
	1. Crear un realme diferente al Master para separarlo de la app
	2. Crear un cliente que se utilizará para la conexión a la app. Dentro del realme => Clients => Create Client
		- `Client type`: `OpenID Connect`
		- `Client ID`: nombre del cliente => Next
		- `Client authentication`: ON
		- `Authorization`: OFF, marcar si se quiere realizar una configuración afinada de roles
		- `Authentication flow`: por defecto => Next
		- `Valid redirect URIs`: * (revisar documentación para rellenar el resto de espacios, de forma preliminar se puede solo modificar este campo con  `*`) => Save
	3. Dentro del cliente, acceder a `Credentials` y copiar el `Secret`, que sera necesario para la configuracion de los endpoints en ApiSix.
	4. Crear un usuario => Users => Add User. Permite pedir validacion por mail, cambio de contraseña al primer inicio...
	5. Una vez creado el usuario, en la pestaña Credentials, configurar una contraseña
3. Acceder al directorio `apisix_conf` y abrir `apisix-standalone.yaml` para configurar los endpoints
4. Se ha dejado una plantilla, debe cambiarse los siguientes datos:
	- `client_id`: por el nombre del cliente creado en KeyCloak
	- `client_secret`: por el secret copiado anteriormente del cliente
	- `discovery`: la url de configuracion para el realme creado, se ha definido como el host de Docker para poder acceder desde fuera de la red al servicio de KeyCloak, que esta expuesto en el puerto 9000. `testrealm` debe reemplazarse por el nombre del realm que se haya creado
	- `realm`: introducir el nombre del realm creado
	- `redirect_uri`: se puede configurar una dirección de redireccionado cuando se valida el login, en este caso, como el nodo apunta a una dirección a la que se accedera despues de login, se ha comentado
	- `scheme`: se debe modificar si desde el proxy se va a acceder a un servicio `http` o `https`
	- `nodes`: se ha puesto `amazon` para pruebas. Admite el DNS de la red de Docker por lo que si hay un servicio `s1.com` podria especificarse. A su vez, para acceder a servicios fuera de la red de Docker admite `host.docker.internal:port` como nodo valido
5. Debe reiniciarse el servidor ApiSix para que recoja los cambios realizados, no asi KeyCloak, que utiliza la base de datos: `docker compose restart apisix`
	
## Pruebas

Una vez realizada la configuracion, se puede probar el endpoint conectando `http://localhost:9080/`. Si todo se ha configurado correctamente debería renderizarse una página de Login de KeyCloak, y, tras rellenar los datos del usuario generado, poder acceder a la web deseada.
