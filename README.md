# Cookit
Aplicación mobile desarrollada en Kotlin para la materia Diseño de aplicaciones, consultando recetas a la API Spoonacular.
Permite ingresar bajo la autenticacion con Google, ver un listado de las recetas disponibles en la API, tocar la imagen de una receta para ver sus ingredientes en detalle, agregar la misma a favoritos.
Almacenando los favoritos en Firebase y Room para que también estén de forma local en el dispositivo.

Para acceder a los datos de la API es necesario registrarse en la misma y agregar al Header de metodo GET la ApiKey generada.

```
    @GET("/recipes/{id}/information/")
    fun getRecipebyID (@Path("id") id : Int,
                       @Header ("x-api-key") apiKey : String)
            : Call<RecipeDetailModel>
```


### Descripción de ENDPOINT Home

URL = https://api.spoonacular.com/recipes/complexSearch
```
Resultado = {
    "results": [
        {
            "id": 716426,
            "title": "Cauliflower, Brown Rice, and Vegetable Fried Rice",
            "image": "https://spoonacular.com/recipeImages/716426-312x231.jpg",
            "imageType": "jpg"
        },
        {
            "id": 715446,
            "title": "Slow Cooker Beef Stew",
            "image": "https://spoonacular.com/recipeImages/715446-312x231.jpg",
            "imageType": "jpg"
        },
        {
            "id": 715415,
            "title": "Red Lentil Soup with Chicken and Turnips",
            "image": "https://spoonacular.com/recipeImages/715415-312x231.jpg",
            "imageType": "jpg"
        }
    ],
    "offset": 0,
    "number": 10,
    "totalResults": 5224
}
```

### Descripcion de ENDPOINT Detalle de receta

URL = https://api.spoonacular.com/recipes/716429/information (donde 716429 es el ID de la receta)
```
Resultado = {
    "vegetarian": false,
    "vegan": false,
    "glutenFree": false,
    "dairyFree": false,
    "veryHealthy": false,
    "cheap": false,
    "veryPopular": false,
    "sustainable": false,
    "lowFodmap": false,
    "weightWatcherSmartPoints": 17,
    "gaps": "no",
    "preparationMinutes": -1,
    "cookingMinutes": -1,
    "aggregateLikes": 209,
    "healthScore": 18,
    "creditsText": "Full Belly Sisters",
    "license": "CC BY-SA 3.0",
    "sourceName": "Full Belly Sisters",
    "pricePerServing": 163.15,
    "extendedIngredients": [
        {
            "id": 1001,
            "aisle": "Milk, Eggs, Other Dairy",
            "image": "butter-sliced.jpg",
            "consistency": "SOLID",
            "name": "butter",
            "nameClean": "butter",
            "original": "1 tbsp butter",
            "originalName": "butter",
            "amount": 1.0,
            "unit": "tbsp",
            "meta": [],
            "measures": {
                "us": {
                    "amount": 1.0,
                    "unitShort": "Tbsp",
                    "unitLong": "Tbsp"
                },
                "metric": {
                    "amount": 1.0,
                    "unitShort": "Tbsp",
                    "unitLong": "Tbsp"
                }
            }
        },
        {
            "id": 1102047,
            "aisle": "Spices and Seasonings",
            "image": "salt-and-pepper.jpg",
            "consistency": "SOLID",
            "name": "salt and pepper",
            "nameClean": "salt and pepper",
            "original": "salt and pepper, to taste",
            "originalName": "salt and pepper, to taste",
            "amount": 2.0,
            "unit": "servings",
            "meta": [
                "to taste"
            ],
            "measures": {
                "us": {
                    "amount": 2.0,
                    "unitShort": "servings",
                    "unitLong": "servings"
                },
                "metric": {
                    "amount": 2.0,
                    "unitShort": "servings",
                    "unitLong": "servings"
                }
            }
        }
    ],
    "id": 716429,
    "title": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs",
    "readyInMinutes": 45,
    "servings": 2,
    "sourceUrl": "http://fullbellysisters.blogspot.com/2012/06/pasta-with-garlic-scallions-cauliflower.html",
    "image": "https://spoonacular.com/recipeImages/716429-556x370.jpg",
    "imageType": "jpg",
    "summary": "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs might be just the main course you are searching for. This recipe makes 2 servings with <b>636 calories</b>, <b>21g of protein</b>, and <b>20g of fat</b> each. For <b>$1.83 per serving</b>, this recipe <b>covers 24%</b> of your daily requirements of vitamins and minerals. From preparation to the plate, this recipe takes about <b>45 minutes</b>. This recipe is liked by 209 foodies and cooks. If you have pasta, salt and pepper, cheese, and a few other ingredients on hand, you can make it. To use up the extra virgin olive oil you could follow this main course with the <a href=\"https://spoonacular.com/recipes/peach-crisp-healthy-crisp-for-breakfast-487698\">Peach Crisp: Healthy Crisp for Breakfast</a> as a dessert. All things considered, we decided this recipe <b>deserves a spoonacular score of 86%</b>. This score is tremendous. Try <a href=\"https://spoonacular.com/recipes/cauliflower-gratin-with-garlic-breadcrumbs-318375\">Cauliflower Gratin with Garlic Breadcrumbs</a>, <a href=\"https://spoonacular.com/recipes/pasta-with-cauliflower-sausage-breadcrumbs-30437\">Pasta With Cauliflower, Sausage, & Breadcrumbs</a>, and <a href=\"https://spoonacular.com/recipes/pasta-with-roasted-cauliflower-parsley-and-breadcrumbs-30738\">Pasta With Roasted Cauliflower, Parsley, And Breadcrumbs</a> for similar recipes.",
    "cuisines": [],
    "dishTypes": [
        "lunch",
        "main course",
        "main dish",
        "dinner"
    ],
    "diets": [],
    "occasions": [],
    "winePairing": {
        "pairedWines": [],
        "pairingText": "No one wine will suit every pasta dish. Pasta in a tomato-based sauce will usually work well with a medium-bodied red, such as a montepulciano or chianti. Pasta with seafood or pesto will fare better with a light-bodied white, such as a pinot grigio. Cheese-heavy pasta can pair well with red or white - you might try a sangiovese wine for hard cheeses and a chardonnay for soft cheeses. We may be able to make a better recommendation if you ask again with a specific pasta dish.",
        "productMatches": []
    },
    "instructions": "",
    "analyzedInstructions": [],
    "originalId": null,
    "spoonacularSourceUrl": "https://spoonacular.com/pasta-with-garlic-scallions-cauliflower-breadcrumbs-716429"
}
```
### Prototipo Figma

https://www.figma.com/file/LCUCwOwbUtWsn9Nma4eiTo/Cookit?node-id=1%3A961&t=6a8uN8OlLnskdJ4f-0

![image](https://user-images.githubusercontent.com/69213023/201830039-143a1984-d849-4595-9927-fb8c26a41635.png)

### App funcionando 🙂

![app Cookit](https://user-images.githubusercontent.com/69213023/204058750-69d58dd5-cc45-4eaf-be55-90dbef922b3e.jpg)

