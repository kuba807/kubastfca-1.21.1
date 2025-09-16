import json
foods=[]
def modelitem(name):
    data = {
  "parent": "item/generated",
  "textures": {
    "layer0": "kubastfca:item/"+name
  }
}
    with open("../src\main/resources/assets/kubastfca/models/item/"+name+'.json', 'w') as file:
        json.dump(data, file)

def simplefood(name,hunger=4,decay=1,sat=4,grain=0,fruit=0,prot=0,veg=0,dairy=0):
    modelitem(name)
    foods.append(name)
    data = {
  "ingredient": {
    "item": "kubastfca:"+name
  },
  "hunger":hunger,
  "decay_modifier":decay,
  "saturation":sat,
  "fruit":fruit,
  "grain": grain,
  "protein": prot,
  "vegetables": veg,
  "dairy": dairy

}
    with open("../src/main/resources/data/kubastfca/tfc/food/" + name + '.json', 'w') as file:
        json.dump(data, file)

simplefood("pemmican",4,0.1,2,prot=1.8,fruit=0.2)
simplefood("cooked_pasta",4,decay=2.5,sat=5,grain=0.5)
simplefood("raw_pasta",2,decay=0.1)

def boilingrecipe(input,output,influid="minecraft:water",temp=300):
    data = {
  "type": "tfc:pot",
  "duration": 1000,
  "fluid_ingredient": {
    "amount": 100,
    "fluid": influid
  },
  "ingredients": [
    {
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }
      ]
    }
  ],
  "item_output": [
    {
      "count": 1,
      "id": "kubastfca:"+output
    }
  ],
  "temperature": temp
}
    with open("../src/main/resources/data/kubastfca/recipe/food/"+output+"_1.json", 'w') as file:
        json.dump(data, file)
    data = {
  "type": "tfc:pot",
  "duration": 1000,
  "fluid_ingredient": {
    "amount": 100,
    "fluid": influid
  },
  "ingredients": [
    {
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    }

  ],
  "item_output": [
    {
      "count": 1,
      "id": "kubastfca:"+output
    },
      {
          "count": 1,
          "id": "kubastfca:" + output
      }
  ],
  "temperature": temp
}
    with open("../src/main/resources/data/kubastfca/recipe/food/"+output+"_2.json", 'w') as file:
        json.dump(data, file)
    data = {
  "type": "tfc:pot",
  "duration": 1000,
  "fluid_ingredient": {
    "amount": 100,
    "fluid": influid
  },
  "ingredients": [
    {
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    }

  ],
  "item_output": [
    {
      "count": 1,
      "id": "kubastfca:"+output
    },
      {
          "count": 1,
          "id": "kubastfca:" + output
      },
      {
          "count": 1,
          "id": "kubastfca:" + output
      }
  ],
  "temperature": temp
}
    with open("../src/main/resources/data/kubastfca/recipe/food/"+output+"_3.json", 'w') as file:
        json.dump(data, file)
    data = {
  "type": "tfc:pot",
  "duration": 1000,
  "fluid_ingredient": {
    "amount": 100,
    "fluid": influid
  },
  "ingredients": [
    {
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    }

  ],
  "item_output": [
    {
      "count": 1,
      "id": "kubastfca:"+output
    },
      {
          "count": 1,
          "id": "kubastfca:" + output
      },
      {
          "count": 1,
          "id": "kubastfca:" + output
      },
      {
          "count": 1,
          "id": "kubastfca:" + output
      }
  ],
  "temperature": temp
}
    with open("../src/main/resources/data/kubastfca/recipe/food/"+output+"_4.json", 'w') as file:
        json.dump(data, file)
    data = {
  "type": "tfc:pot",
  "duration": 1000,
  "fluid_ingredient": {
    "amount": 100,
    "fluid": influid
  },
  "ingredients": [
    {
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    },{
      "type": "tfc:and",
      "children": [
        {
          "item": "kubastfca:"+input
        },
        {
          "type": "tfc:not_rotten"
        }

      ]
    }

  ],
  "item_output": [
    {
      "count": 1,
      "id": "kubastfca:"+output
    },
      {
          "count": 1,
          "id": "kubastfca:" + output
      },
      {
          "count": 1,
          "id": "kubastfca:" + output
      },
      {
          "count": 1,
          "id": "kubastfca:" + output
      },
      {
          "count": 1,
          "id": "kubastfca:" + output
      }
  ],
  "temperature": temp
}
    with open("../src/main/resources/data/kubastfca/recipe/food/"+output+"_5.json", 'w') as file:
        json.dump(data, file)

boilingrecipe("raw_pasta","cooked_pasta",influid="tfc:salt_water")


food=[]
for i in foods:
    food.append("kubastfca:"+i)
    data ={
  "replace": False,
  "values": food
}
    with open("../src/main/resources/data/c/tags/item/foods.json", 'w') as file:
        json.dump(data, file)