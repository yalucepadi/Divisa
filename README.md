[Divisas.postman_collection.json](https://github.com/user-attachments/files/17170842/Divisas.postman_collection.json)# RETO TÉCNICO BACKEND 

## 1.Introduccion 

El proyecto consiste en convertir de una moneda de origen  a otra con un tipo de cambio dando por resultado el valor en la moneda destino.

## 2.Requisitos

### 2.1 Requisitos Funcionales
  * El Sistema permite ingresar la moneda de origen.
  * El Sistema permite ingresar la moneda destino.
  * El Sistema permite ingresar el monto.
  * El Sistema permite ingresar el tipo de cambio.
  * El sistema muestra el monto final  convertido en la moneda destino.
  * El Sistema permite actualizr el tipo de cambio a si mismo actualiza el monto final  convertido en la moneda destino.
### 2.2 Herramientas
  * Java 17
  * Framework : Spring Boot v3
  * Spring data JPA
  * Database H2
  * Lombok
  * Mapstruct
  * Mockito
  * IDE IntelliJ IDEA
  * Git
    
## 3.Ejecución

Basta con abrir el IDE y ejecutarlo.


[Uploading Divisas.postman_collection.jso{
	"info": {
		"_postman_id": "17f1080a-e7df-4871-a720-3ca1cb032a4e",
		"name": "Divisas",
		"schema": "https://schema.getpostman.com/json/collection/v2.0.0/collection.json",
		"_exporter_id": "22884094",
		"_collection_link": "https://speeding-sunset-656617.postman.co/workspace/BankEnd~dd2bb3ca-0dc2-443e-885a-a5a95c7fbd09/collection/22884094-17f1080a-e7df-4871-a720-3ca1cb032a4e?action=share&source=collection_link&creator=22884094"
	},
	"item": [
		{
			"name": "getDivisas",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "http://localhost:9090/api/divisas"
			},
			"response": []
		},
		{
			"name": "getDivisaById",
			"protocolProfileBehavior": {
				"disableBodyPruning": true
			},
			"request": {
				"method": "GET",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "http://localhost:9090/api/divisa/2"
			},
			"response": []
		},
		{
			"name": "DeleteDivisaById",
			"request": {
				"method": "DELETE",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "http://localhost:9090/api/divisa/2"
			},
			"response": []
		},
		{
			"name": "cambio",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"monedaOrigen\": \"pipas\",\r\n    \"monedaDestino\": \"Dolares\",\r\n    \"monto\": 45.0,\r\n    \"precioDeCambio\": 1.2\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": "http://localhost:9090/api/cambio"
			},
			"response": [
				{
					"name": "ok",
					"originalRequest": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"monedaOrigen\": \"chupetes\",\r\n    \"monedaDestino\": \"Dolares\",\r\n    \"monto\": 47.0,\r\n    \"precioDeCambio\": 3.2\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": "http://localhost:9090/api/cambio"
					},
					"status": "OK",
					"code": 200,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json"
						},
						{
							"key": "Transfer-Encoding",
							"value": "chunked"
						},
						{
							"key": "Date",
							"value": "Fri, 27 Sep 2024 12:16:27 GMT"
						},
						{
							"key": "Keep-Alive",
							"value": "timeout=60"
						},
						{
							"key": "Connection",
							"value": "keep-alive"
						}
					],
					"cookie": [],
					"body": "{\n    \"monedaOrigen\": \"chupetes\",\n    \"monedaDestino\": \"Dolares\",\n    \"resultado\": 150.4,\n    \"comentario\": \"El cambio total de chupetes a Dolares es 150.4 Dolares\"\n}"
				},
				{
					"name": "errorDatos",
					"originalRequest": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"monedaOrigen\": \"chupetes\",\r\n    \"monedaDestino\": \"Dolares\",\r\n    \"monto\": 47.0,\r\n    \"precioDeCambio\": 0.0\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": "http://localhost:9090/api/cambio"
					},
					"status": "Internal Server Error",
					"code": 500,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json"
						},
						{
							"key": "Transfer-Encoding",
							"value": "chunked"
						},
						{
							"key": "Date",
							"value": "Fri, 27 Sep 2024 12:59:34 GMT"
						},
						{
							"key": "Connection",
							"value": "close"
						}
					],
					"cookie": [],
					"body": "{\n    \"error\": \"Server Error\",\n    \"message\": \"Error en el servidor: El resultado del cálculo no es un número válido\",\n    \"status\": 500\n}"
				},
				{
					"name": "ErrorResponse",
					"originalRequest": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"monedaOrigen\": \"chupetes\",\r\n    \"monedaDestino\": \"Dolares\",\r\n    \"monto\": 47.0\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": "http://localhost:9090/api/cambio"
					},
					"status": "Internal Server Error",
					"code": 500,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json"
						},
						{
							"key": "Transfer-Encoding",
							"value": "chunked"
						},
						{
							"key": "Date",
							"value": "Fri, 27 Sep 2024 12:59:51 GMT"
						},
						{
							"key": "Connection",
							"value": "close"
						}
					],
					"cookie": [],
					"body": "{\n    \"error\": \"Server Error\",\n    \"message\": \"Error en el servidor: Cannot invoke \\\"java.lang.Double.doubleValue()\\\" because the return value of \\\"com.cambio.divisas.domain.Divisa.getPrecioDeCambio()\\\" is null\",\n    \"status\": 500\n}"
				}
			]
		},
		{
			"name": "actualizar-TipoDcambio",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:9090/api/actualizarTipoDeCambio?id=1&nuevoTipoDeCambio=2.2",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "9090",
					"path": [
						"api",
						"actualizarTipoDeCambio"
					],
					"query": [
						{
							"key": "id",
							"value": "1"
						},
						{
							"key": "nuevoTipoDeCambio",
							"value": "2.2"
						}
					]
				}
			},
			"response": [
				{
					"name": "AddDivisa Copy 3",
					"originalRequest": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:9090/api/actualizarTipoDeCambio?id=1&nuevoTipoDeCambio=5.6 ",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "9090",
							"path": [
								"api",
								"actualizarTipoDeCambio"
							],
							"query": [
								{
									"key": "id",
									"value": "1"
								},
								{
									"key": "nuevoTipoDeCambio",
									"value": "5.6 "
								}
							]
						}
					},
					"status": "OK",
					"code": 200,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json"
						},
						{
							"key": "Transfer-Encoding",
							"value": "chunked"
						},
						{
							"key": "Date",
							"value": "Fri, 27 Sep 2024 20:53:00 GMT"
						},
						{
							"key": "Keep-Alive",
							"value": "timeout=60"
						},
						{
							"key": "Connection",
							"value": "keep-alive"
						}
					],
					"cookie": [],
					"body": "{\n    \"monedaOrigen\": \"Dolares\",\n    \"monedaDestino\": \"Soles\",\n    \"resultado\": 263.2,\n    \"comentario\": \"Divisa actualizada correctamente\"\n}"
				},
				{
					"name": "AddDivisa Copy 3",
					"originalRequest": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:9090/api/actualizarTipoDeCambio?id=2&nuevoTipoDeCambio=5.6 ",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "9090",
							"path": [
								"api",
								"actualizarTipoDeCambio"
							],
							"query": [
								{
									"key": "id",
									"value": "2"
								},
								{
									"key": "nuevoTipoDeCambio",
									"value": "5.6 "
								}
							]
						}
					},
					"status": "Bad Request",
					"code": 400,
					"_postman_previewlanguage": "json",
					"header": [
						{
							"key": "Content-Type",
							"value": "application/json"
						},
						{
							"key": "Transfer-Encoding",
							"value": "chunked"
						},
						{
							"key": "Date",
							"value": "Fri, 27 Sep 2024 20:53:27 GMT"
						},
						{
							"key": "Connection",
							"value": "close"
						}
					],
					"cookie": [],
					"body": "{\n    \"error\": \"Divisa Error\",\n    \"message\": \"Por favor revisa los datos ingresados.\",\n    \"status\": 400\n}"
				}
			]
		}
	]
}n…]()

    
