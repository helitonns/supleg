# Sistema SupLeg
Esse sistema é usado pela Superitência Legislativa, para a geração do roteiro que é lido nas sessões plenárias. 


## Configuração do Standalone.xml

```

<datasource jta="true" jndi-name="java:/jboss/datasources/alrr-dev-ds" pool-name="alrr-dev-ds" enabled="true" use-java-context="true" spy="true" use-ccm="false">
	<connection-url>jdbc:postgresql://localhost/alrr-dev</connection-url>
	<driver-class>org.postgresql.Driver</driver-class>
	<driver>postgresql</driver>
	<security>
    		<user-name>postgres</user-name>
    		<password>postgres</password>
	</security>
    <validation>
    		<valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker"/>
        <check-valid-connection-sql>SELECT 1</check-valid-connection-sql>
        <background-validation>true</background-validation>
        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter"/>
	</validation>
    <timeout>
    		<blocking-timeout-millis>360</blocking-timeout-millis>
	</timeout>
</datasource>

<drivers>
	...
	<driver name="postgresql" module="org.postgresql">
		<driver-class>org.postgresql.Driver</driver-class>
		<xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
	</driver>
</drivers>

```

## Configuração Maven

Existem 3 profiles:
 - desenvolvimento:
 - homologacao: selecione quando for gerar o arquivo ``supleg.war``
 - producao:

## Ambientes

**Desenvolvimento**

 - execute o script ``/srvc/main/resources/sql/create_schema.sql`` para criar o banco de dados. 
 - o usuário e senha de acesso é ``admin`` ``admin``.
 - no arquivo ``pom.xml``, selecione o **profile** ``<id>desenvolvimento</id>``


```