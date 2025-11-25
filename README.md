# Mutant Analyzer
## Descripción del Proyecto
Magneto quiere reclutar tantos mutantes como sea posible para luchar contra los X-Men. Este proyecto es una API REST diseñada para ayudarlo a detectar si un humano es un mutante basándose en su secuencia de ADN.

El sistema recibe como parámetro un array de Strings que representan cada fila de una tabla de (NxN) con la secuencia del ADN. Las letras de los Strings solo pueden ser: (A,T,C,G), las cuales representa cada base nitrogenada.

Condición de Mutante: Se sabrá si un humano es mutante si se encuentra más de una secuencia de cuatro letras iguales, de forma oblicua, horizontal o vertical.

Ejemplo de ADN Mutante

```json
[
"ATGCGA",
"CAGTGC",
"TTATGT",
"AGAAGG",
"CCCCTA",
"TCACTG"
]
```

En este caso, existen secuencias de 4 letras iguales en varias direcciones, por lo que el sujeto es un MUTANTE.

## Tecnologías Utilizadas

Este proyecto ha sido desarrollado utilizando las siguientes tecnologías y herramientas:

- Lenguaje: Java 21
- Framework: Spring Boot (Web, Data JPA)
- Base de Datos: H2 (para desarrollo/tests) o PostgreSQL (producción)
- Build Tool: Gradle

## Instalación y Ejecución

### Prerrequisitos

- Tener instalado Java JDK 21.
- (Opcional) Cliente API como Postman o cURL.

### Pasos para correr localmente

1. Clonar el repositorio:

```Bash
    git clone https://github.com/TomCab98/utn-mutant-analyzer.git
    cd utn-mutant-analyzer
```

2. Compilar el proyecto:

```Bash
    ./gradlew clean install
```

3. Ejecutar la aplicación:

```Bash
    ./gradlew spring-boot:run
```

La aplicación iniciará generalmente en http://localhost:8080.

## Documentación de la API

La API expone dos endpoints principales:

1. Detectar Mutante

Endpoint: POST /mutant/analizar  
Descripción: Detecta si un humano es mutante basándose en su ADN.

Body (JSON):

```JSON
{
  "nombre": "John",
  "apellido": "Doe",
  "adn": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
```
Respuestas:

200 OK: Es un mutante.
403 Forbidden: No es un mutante (es humano).  
400 Bad Request: ADN inválido (caracteres erróneos o tabla no cuadrada).

Ejemplo con cURL:

```Bash
    curl -X POST "http://localhost:8080/mutant" \
    -H "Content-Type: application/json" \
    -d '{"dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]}'
```

2. Estadísticas


Endpoint: GET /stats
Descripción: Devuelve estadísticas de las verificaciones de ADN realizadas.
Respuesta (JSON):

```JSON
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}
```

Ejemplo con cURL:

```Bash
    curl -X GET "http://localhost:8080/stats"
```

---