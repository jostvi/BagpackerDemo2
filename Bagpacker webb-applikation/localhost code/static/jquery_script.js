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
      $('#item_list' + i).append('<li><input type="text" name="iname" class="item-box"><button type="button" class="add_item" onclick="addItem(this)" >Add item</button> <button onclick="removeItem(this)" class="deleteButton">Delete</button><li>')
    });
  }
});

function addItem(elem) {

  const category = $(elem).parent().parent().parent().attr('id');
  const currentLocalStorage = JSON.parse(localStorage.getItem(category)) || [];

  const categoryIndex = category.substring(3, 4);
  const value = $(elem).siblings()[0].value;
  if (value == "") {
  } else {
    $('#item_list' + categoryIndex).append('<li>' + value + '<button onclick="removeItem(this)" class="deleteButton">Delete</button></li>');
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
        $('#item_list' + i).append('<li>' + hydrateItem + '</li>');
      }
    }
  }
};


function removeItem(elem) {
  const category = $(elem).parent().parent().parent().attr('id');
  console.log(category)
  const dehydrated = JSON.parse(localStorage.getItem(category))
  console.log(dehydrated);
  const value = $(elem).parent().text();
  const filteredItems = dehydrated.filter(item => {
    return value != (item + 'Delete');
  });
  console.log(filteredItems);

  localStorage.setItem(category, JSON.stringify(filteredItems));

  $(elem).parent().remove();
};

// function removeItem(elem) {
//   const category = $(elem).parent().parent().parent().attr('id');
//   $(elem).parent().remove();
// };

$(function () {
  deleteButton = $('<input type="image" src="/static/delete.png" alt="delete" onclick="removeItem(this)" class="deleteButton" />')
  deleteButton.appendTo('ul#item_list1 li');
});

$(function () {
  deleteButton = $('<input type="image" src="/static/delete.png" alt="delete" onclick="removeItem(this)" class="deleteButton" />')
  deleteButton.appendTo('ul#item_list2 li');
});

$(function () {
  deleteButton = $('<input type="image" src="/static/delete.png" alt="delete" onclick="removeItem(this)" class="deleteButton" />')
  deleteButton.appendTo('ul#item_list3 li');
});

$(function () {
  deleteButton = $('<input type="image" src="/static/delete.png" alt="delete" onclick="removeItem(this)" class="deleteButton" />')
  deleteButton.appendTo('ul#item_list4 li');
});

$(function () {
  deleteButton = $('<input type="image" src="/static/delete.png" alt="delete" onclick="removeItem(this)" class="deleteButton" />')
  deleteButton.appendTo('ul#item_list5 li');
});

$(function () {
  deleteButton = $('<input type="image" src="/static/delete.png" alt="delete" onclick="removeItem(this)" class="deleteButton" />')
  deleteButton.appendTo('ul#item_list6 li');
});

const submitList = () => {
  console.log('submitlist')
  const request = {};
  for(let i = 1; i <= 6; i++) {
    const category = 'kat' + i;
    const dataFromLS = JSON.parse(localStorage.getItem(category));
    request[category] = dataFromLS;
  }
  console.log(request);
  const url = 'localhost:8080/'
  fetch(url)
  
}

showLocal();