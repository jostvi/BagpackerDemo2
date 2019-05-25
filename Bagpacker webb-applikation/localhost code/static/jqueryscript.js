$.validate({
    lang : 'sv',
    modules : 'security', 
    form : '#multi-step-form', 
});

$(document).ready(function(){
    // add/remove checked class
    $(".image-radio").each(function(){
        if($(this).find('input[type="radio"]').first().attr("checked")){
            $(this).addClass('image-radio-checked');
        }else{
            $(this).removeClass('image-radio-checked');
        }
    });

    // sync the input state
    $(".image-radio").on("click", function(e){
        $(".image-radio").removeClass('image-radio-checked');
        $(this).addClass('image-radio-checked');
        var $radio = $(this).find('input[type="radio"]');
        $radio.prop("checked",!$radio.prop("checked"));

        e.preventDefault();
    });

    // add/remove checked class
    $(".image-checkbox").each(function(){
        if($(this).find('input[type="checkbox"]').first().attr("checked")){
            $(this).addClass('image-checkbox-checked');
        }else{
            $(this).removeClass('image-checkbox-checked');
        }
    });

    // sync the input state
    $(".image-checkbox").on("click", function(e){
        $(this).toggleClass('image-checkbox-checked');
        var $checkbox = $(this).find('input[type="checkbox"]');
        $checkbox.prop("checked",!$checkbox.prop("checked"));
    
        e.preventDefault();
    });

});

// Hoppa fram & tillbaka i formuläret

$("#multi-step-form fieldset").hide();
$("#fieldset1").show()

$("#destination").on("keyup change", function() {
    if ($(this).val() != "") {
        $("#destination-error").text("");
    }
});

$("#fieldset1 .next").on("click", function() {
    const url = '/validate_destination';
    const destination = $("#destination").val();

    $.ajax({
        url: url,
        type: "POST",
        dataType: "JSON",
        data: {
            destination: destination
        }
    }).done(function(data) {
        if (!data.valid) {
            $("#destination-error").text(data.message);
            return;
        } else {
            // Allt ser bra ut!
            $("#destination-error").text("");

            $("#fieldset1").hide();
            $("#current-form-state span").first().addClass("activedot");
            $("#fieldset2").show();
        }
    });
});

$("#to, #from").on("keyup change", function() {
    if ($(this).val() != "") {
        $("#to-error").text("");
    }
});

$("#fieldset2 .next").on("click", function() {

    var nrOfEl = $(".datefields input[class='error']").length;

    if($("#to").val() != "" && $("#from").val() != "" && nrOfEl == 0) {
        $("#fieldset2").hide();
        $("#current-form-state span").first().next().addClass("activedot");
        $("#fieldset3").show();
        $("#to-error").text("");
    }
    else $("#to-error").text("Du måste ange datum i formatet åååå/mm/dd!");

});
$("#fieldset2 .previous").on("click", function() {
    $("#fieldset2").hide();
    $("#current-form-state span").first().removeClass("activedot");
    $("#fieldset1").show();
});

$("#fieldset3 .next").on("click", function() {
    if($('#fieldset3 input[name=transport]:checked').length > 0) {
        $("#fieldset3").hide();
        $("#current-form-state span").first().next().next().addClass("activedot");
        $("#fieldset4").show();
        $("#transport_validation").text(" ");
    }
    else {
        $("#transport_validation").text("Du måste ange minst ett alternativ!");
    }
});
$("#fieldset3 .previous").on("click", function() {
    $("#fieldset3").hide();
    $("#current-form-state span").first().next().removeClass("activedot");
    $("#fieldset2").show();
});

$("#fieldset4 .next").on("click", function() {
    if($('#fieldset4 input[name=accommodation]:checked').length > 0) {
        $("#fieldset4").hide();
        $("#current-form-state span").first().next().next().next().addClass("activedot");
        $("#fieldset5").show();
        $("#accommodation_validation").text(" ");
    }
    else {
        $("#accommodation_validation").text("Du måste ange minst ett alternativ!");
    }
});
$("#fieldset4 .previous").on("click", function() {
    $("#fieldset4").hide();
    $("#current-form-state span").first().next().next().removeClass("activedot");
    $("#fieldset3").show();
});

$("#submit-form-btn").on("click", function(e) {
    e.preventDefault();

    if($('#fieldset5 input[name=activities]:checked').length > 0) {
        $("#multi-step-form")[0].submit();
    }

    else {
        $("#activity_validation").text("Du måste ange minst ett alternativ!");
    }   
});
$("#fieldset5 .previous").on("click", function() {
    $("#fieldset5").hide();
    $("#current-form-state span").first().next().next().next().removeClass("activedot");
    $("#fieldset4").show();
});


$("hamburgericon").on("click"), function() {
    
}