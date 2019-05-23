$(function () {
  for (let i = 1; i <= 6; i++) {
    $('#myFunction' + i).on('click', function () {
      $(this).toggleClass("rotate")
      $('#kat' + i).slideToggle(200);
    });
  }
});

$(function () {
  for (let i = 1; i <= 6; i++) {
    $('#btn-item' + i).on('click', function () {
      $('#item_list' + i).prepend('<li>' + '<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton2" onclick="removeItem(this)">' + '<input type="text" name="iname" class="item-box">' + '<button type="button" class="add_item" onclick="addItem(this)" >Add' + '<li>')
    });
  }
});

{/* <button onclick="removeItem(this)" class="deleteButton">Delete</button> */ }
function addItem(elem) {

  const category = $(elem).parent().parent().parent().attr('id');
  const currentLocalStorage = JSON.parse(localStorage.getItem(category)) || [];

  const categoryIndex = category.substring(3, 4);
  const value = $(elem).siblings()[1].value;
  if (value == "") {
  } else {
    $('#item_list' + categoryIndex).append('<li class="edit_list">' + '<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">' + '<div class="editalign_left">' + value + '</div>' + '<div class="editalign_right"><input type="number" class="input_quantity" name="quantity" min="1"></div"</li>');
    currentLocalStorage.push(value);
    const stringifiedArray = JSON.stringify(currentLocalStorage);
    localStorage.setItem(category, stringifiedArray);
  }
  removeItem(elem);
}


function showLocal() {
  for (let i = 1; i <= 6; i++) {
    const dehydrateLocal = JSON.parse(localStorage.getItem('kat' + i));
    if (dehydrateLocal != null) {
      for (let j = 0; j < dehydrateLocal.length; j++) {
        const hydrateItem = dehydrateLocal[j];
        $('#item_list' + i).append('<li class="edit_list">' + '<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">' + '<div class="editalign_left">' + hydrateItem + '</div>' + '<div class="editalign_right"><input type="number" class="input_quantity" name="quantity" min="1"></div>' + '</li>');
      }
    }
  }
};


function removeItem(elem) {
  const category = $(elem).parent().parent().parent().attr('id');
  console.log(category)
  const dehydrated = JSON.parse(localStorage.getItem(category));
  if (dehydrated == null) {
    $(elem).parent().remove();
  }
  else {
    console.log(dehydrated);
    const value = $(elem).parent().text();
    const filteredItems = dehydrated.filter(item => {
      return value != item;
    });
    console.log(filteredItems);

    localStorage.setItem(category, JSON.stringify(filteredItems));

    $(elem).parent().remove();
  }
};


// function emptyLocal(elem) {
//   const category = $(elem).parent().parent().parent().attr('id');
//   console.log(category)
// }

// function removeItem(elem) {
//   const category = $(elem).parent().parent().parent().attr('id');
//   $(elem).parent().remove();
// };

// $(function () {
//   deleteButton = $('<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">')
//   deleteButton.appendTo('ul#item_list1 li');
// });

// $(function () {
//   deleteButton = $('<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">');
//   deleteButton.appendTo('ul#item_list2 li');
// });

// $(function () {
//   deleteButton = $('<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">');
//   deleteButton.appendTo('ul#item_list3 li');
// });

// $(function () {
//   deleteButton = $('<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">');
//   deleteButton.appendTo('ul#item_list4 li');
// });

// $(function () {
//   deleteButton = $('<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">');
//   deleteButton.appendTo('ul#item_list5 li');
// });

// $(function () {
//   deleteButton = $('<input type="image" src="../static/delete.png" alt="Submit" class="deleteButton" onclick="removeItem(this)">');
//   deleteButton.appendTo('ul#item_list6 li');
// });

// function sendList(){
// const submitList = () => {
//   console.log('submitlist')
//   const request = {};
//   for(let i = 1; i <= 6; i++) {
//     const category = 'kat' + i;
//     const dataFromLS = JSON.parse(localStorage.getItem(category));
//     request[category] = dataFromLS;
//   }
//   console.log(request);
//   const url = 'localhost:8080/'
//   fetch(url, {
//     method: 'POST'
//   })

// // }
// }



// function populateLocalStorage() {
//   localStorage.setItem('kat1', JSON.stringify([]));
//   localStorage.setItem('kat2', JSON.stringify([]));
//   localStorage.setItem('kat3', JSON.stringify([]));
//   localStorage.setItem('kat4', JSON.stringify([]));
//   localStorage.setItem('kat5', JSON.stringify([]));
//   localStorage.setItem('kat6', JSON.stringify([]));
// }

showLocal();