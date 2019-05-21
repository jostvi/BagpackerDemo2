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