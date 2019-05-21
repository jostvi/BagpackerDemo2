var currentTab = 0;
showTab(currentTab);

function showTab(n) {
    var x = document.getElementsByClassName("step");
    x[n].style.display = "block";

    if (n == 0) {
        document.getElementById("prevBtn").style.display = "none";
    } else {
        document.getElementById("prevBtn").style.display = "inline";
    }
    if (n == (x.length -1)) {
        document.getElementById("nextBtn").innerHTML = "Generera packlista!";
    } else {
        document.getElementById("nextBtn").innerHTML = "Nästa";
    }
    
    fixStepIndicator(n)
}

async function nextPrev(n) {
    // kolla switch case
    var x = document.getElementsByClassName("step");
    if (n == 1 && !validateForm()) return false;
    const url = '/validate_destination';
    const destination = document.getElementById('destination').value;
    const data = {
        destination,
    };
    const response = await fetch(url, {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, cors, *same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json',
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: 'follow', // manual, *follow, error
        referrer: 'no-referrer', // no-referrer, *client
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    });
    const parsedResponse = await response.json();
    if (!parsedResponse.valid) {
        var text
        text = "Destinationen du angav är inte giltig."

        document.getElementById("destination-error").innerHTML = text;
        return;
    }
    else {
        var text
        text = "Giltig destination."

        document.getElementById("destination-error").innerHTML = text;
    }
    x[currentTab].style.display = "none";
    currentTab = currentTab + n; 
    if (currentTab >= x.length) {
        document.getElementById("multi-step-form").submit();
        return false;
    }
    showTab(currentTab);
}

function validateForm() {
    var x, y, i, valid = true;
    x = document.getElementsByClassName("step");
    y = x[currentTab].getElementsByTagName("input");
    for (i = 0; i < y.length; i++) {
        //kan man lägga in fler saker här???
        if (y[i].value == "") {
            y[i].className += " invalid";
            valid = false;
        }
    }
    if (valid) {
        document.getElementsByClassName("dots")[currentTab].className += " finish";
    }
    return valid;
}

function fixStepIndicator(n) {
        var i, x = document.getElementsByClassName("dots");
        for (i = O; i < x.length; i++) {
            x[i].className = x[i].className.replace(" active", "");
        }   
        x[n].className += " active";
    }

function validateTransportCheckboxes() { 
    
    if (!document.getElementById("transport1").checked && !document.getElementById("transport2").checked
        && !document.getElementById("transport3").checked && !document.getElementById("transport4").checked
        && !document.getElementById("transport5").checked && !document.getElementById("transport6").checked
        && !document.getElementById("transport7").checked && !document.getElementById("transport8").checked) {
        var text
        text = "Du måste välja minst ett alternativ"

        document.getElementById("transport_validation").innerHTML = text;
        document.getElementById("nextBtn").disabled = true;

        }
    else {
        document.getElementById("nextBtn").disabled = false;
    }
};

function validateAccoCheckboxes() { 
    
    if (!document.getElementById("accommodation1").checked && !document.getElementById("accommodation2").checked
        && !document.getElementById("accommodation3").checked && !document.getElementById("accommodation4").checked
        && !document.getElementById("accommodation5").checked && !document.getElementById("accommodation6").checked
        && !document.getElementById("accommodation7").checked && !document.getElementById("accommodation8").checked) {
        var text
        text = "Du måste välja minst ett alternativ"

        document.getElementById("accommodation_validation").innerHTML = text;
        document.getElementById("nextBtn").disabled = true;

        }
    else {
        document.getElementById("nextBtn").disabled = false;
    }
}

function validateActivityCheckboxes() { 
    
    if (!document.getElementById("activity1").checked && !document.getElementById("activity2").checked
        && !document.getElementById("activity3").checked && !document.getElementById("activity4").checked
        && !document.getElementById("activity5").checked && !document.getElementById("activity6").checked
        && !document.getElementById("activity7").checked && !document.getElementById("activity8").checked) {
        var text
        text = "Du måste välja minst ett alternativ"

        document.getElementById("activity_validation").innerHTML = text;
        document.getElementById("nextBtn").disabled = true;

        }
    else {
        document.getElementById("nextBtn").disabled = false;
    }
}
