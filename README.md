 ### Docs

- Support Standard Markdown / CommonMark and GFM(GitHub Flavored Markdown);
- Nice Hardware API es un metabuscador que recopila informacion de componentes de pc en paralelo en algunos de los ecommerces y/o depositos de tecnologia mas importantes de argentina, para que el usuario pueda comparar entre precios, tiempos de envio y variedades de productos entre otras funcionalidades.

- La API esta desarrollada enteramente en Java con Springboot, Maven, StreamAPI, Selenium, entre otras librerias y deployada en la nube con Linode (Linux), Docker, Nginx, Certbot, etc.

###Endpoint

`<GET>` : <https://nice-hardware.tk/search>

#### Query Params

`<query>` : Nombre del producto por ej: 'Disco SSD Kingston'
`<items>` : Cantidad de items a buscar por sitio, por ej: '5'

### URL ejemplo

> https://nice-hardware.tk/search?query=i7&items=2
