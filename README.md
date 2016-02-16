Scala playframework and slick
==========================================
[![Build Status](https://travis-ci.org/kozlov-p-v/car-adverts.svg?branch=master)](https://travis-ci.org/kozlov-p-v/car-adverts)
[![Coverage Status](https://coveralls.io/repos/kozlov-p-v/car-adverts/badge.png?branch=master)](https://coveralls.io/r/kozlov-p-v/car-adverts)

# Fiori-blossoms RESTful service
To run application just type "sbt run"

Application supports the following methods:
GET /products - list all products
GET /products/{id} - find by id
DELETE /products/{id} - delete by id
PUT /products/{id} - update (modify) existing product
POST /products - create new product


Application uses H2 embedded database in-memory. No need to prepare anything.

