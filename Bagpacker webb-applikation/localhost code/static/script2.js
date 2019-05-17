/* 
Orginal Page: http://thecodeplayer.com/walkthrough/jquery-multi-step-form-with-progress-bar 

*/
//jQuery time
//$(document).ready(function() {
	var current_fs, next_fs, previous_fs; //fieldsets
	var left, opacity, scale; //fieldset properties which we will animate
	var animating; //flag to prevent quick multi-click glitches
	
	$(".steps").validate({
        errorClass: 'invalid',
        errorElement: 'span',
        errorPlacement: function(error, element) {
            error.insertAfter(element.next('span').children());
        },
        highlight: function(element) {
            $(element).next('span').show();
        },
        unhighlight: function(element) {
			$(element).next('span').hide();
		},

		rules: {
			'transport': {
				required: true,
				minlength: 1,
			
			}
		},
		messages: {
			'transport': {
				required: "Du måste välja minst ett alternativ",
			}
		}
    });

	$(".submit").click(function() {
        $(".steps").validate({
            errorClass: 'invalid',
            errorElement: 'span',
            errorPlacement: function(error, element) {
                error.insertAfter(element.next('span').children());
            },
            highlight: function(element) {
                $(element).next('span').show();
            },
            unhighlight: function(element) {
                $(element).next('span').hide();
			},
			rules: {
				'transport': {
					required: true,
					minlength: 1,
				
				}
			},
			messages: {
				'transport': {
					required: "Du måste välja minst ett alternativ",
				}
			}
        });
        if ((!$('.steps').valid())) {
            return false;
        }
        if (animating) return false;
        animating = true;
        current_fs = $(this).parent();
        next_fs = $(this).parent().next();
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
        next_fs.show();
        current_fs.animate({
            opacity: 0
        }, {
            step: function(now, mx) {
                scale = 1 - (1 - now) * 0.2;
                left = (now * 50) + "%";
                opacity = 1 - now;
                current_fs.css({
                    'transform': 'scale(' + scale + ')'
                });
                next_fs.css({
                    'left': left,
                    'opacity': opacity
                });
            },
            duration: 800,
            complete: function() {
                current_fs.hide();
                animating = false;
            },
            easing: 'easeInOutExpo'
        });
    });

	$(".next").click(function(){
 		$(".steps").validate({
             errorClass: 'invalid',
             errorElement: 'span',
             errorPlacement: function(error, element) {
                 error.insertAfter(element.next('span').children());
             },
             highlight: function(element) {
                 $(element).next('span').show();
             },
             unhighlight: function(element) {
                 $(element).next('span').hide();
		 	},
		 	rules: {
		 		'transport': {
		 			required: true,
		 			minlength: 1,
				
		 		}
		 	},
		 	messages: {
		 		'transport': {
		 			required: "Du måste välja minst ett alternativ",
		 		}
		 	}
		 });
		 //JS för att kunna visa upp felmeddelande på destination
		if (document.getElementById("address-input").value.length <= 0) {
			const errorMessage = document.getElementsByClassName('error1')[0];
			errorMessage.setAttribute('style', 'display: block;');
			errorMessage.innerText = 'Du måste ange ett resmål!';
			errorMessage
			return;
		}
        if ((!$('.steps').valid())) {
            return false;
        }
		if(animating) return false;
		animating = true;
		
		current_fs = $(this).parent();
		next_fs = $(this).parent().next();
		
		//activate next step on progressbar using the index of next_fs
		$("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
		
		//show the next fieldset
		next_fs.show(); 
		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now, mx) {
				//as the opacity of current_fs reduces to 0 - stored in "now"
				//1. scale current_fs down to 80%
				scale = 1 - (1 - now) * 0.2;
				//2. bring next_fs from the right(50%)
				left = (now * 50)+"%";
				//3. increase opacity of next_fs to 1 as it moves in
				opacity = 1 - now;
				current_fs.css({'transform': 'scale('+scale+')'});
				next_fs.css({'left': left, 'opacity': opacity});
			}, 
			duration: 800, 
			complete: function(){
				current_fs.hide();
				animating = false;
			}, 
			//this comes from the custom easing plugin
			easing: 'easeInOutBack'
		});
	//});

	$(".previous").click(function(){
		if(animating) return false;
		animating = true;
		
		current_fs = $(this).parent();
		previous_fs = $(this).parent().prev();
		
		//de-activate current step on progressbar
		$("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
		
		//show the previous fieldset
		previous_fs.show(); 
		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now, mx) {
				//as the opacity of current_fs reduces to 0 - stored in "now"
				//1. scale previous_fs from 80% to 100%
				scale = 0.8 + (1 - now) * 0.2;
				//2. take current_fs to the right(50%) - from 0%
				left = ((1-now) * 50)+"%";
				//3. increase opacity of previous_fs to 1 as it moves in
				opacity = 1 - now;
				current_fs.css({'left': left});
				previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
			}, 
			duration: 800, 
			complete: function(){
				current_fs.hide();
				animating = false;
			}, 
			//this comes from the custom easing plugin
			easing: 'easeInOutBack'
			});
		});
});	

/* 	$(".submit").click(function(){
		return false;
	})

 */

/*Kalender */

$( function() {
	$.datepicker.setDefaults($.datepicker.regional['sv']);
	var dateFormat = "yy/mm/dd",
	from = $( "#from" )
		.datepicker({
		dateFormat: "yy/mm/dd",
		showAnim: "slideDown",
		defaultDate: "0",
		changeMonth: true,
		numberOfMonths: 1, 
		minDate: 0
		})
		.on( "change", function() {
		to.datepicker( "option", "minDate", getDate( this ) );
		}),
	to = $( "#to" ).datepicker({
		defaultDate: "+1w",
		dateFormat: "yy/mm/dd",
		showAnim: "slideDown",
		changeMonth: true,
		numberOfMonths: 2,
		minDate: 0
	})
	.on( "change", function() {
		from.datepicker( "option", "maxDate", getDate( this ) );
	
	});
	
	function getDate( element ) {
	var date;
	try {
		date = $.datepicker.parseDate( dateFormat, element.value );
	} catch( error ) {
		date = null;
	}

	return date;
	}
} );


/* Swedish initialisation for the jQuery UI date picker plugin. */
/* Written by Anders Ekdahl ( anders@nomadiz.se). */
( function( factory ) {
	if ( typeof define === "function" && define.amd ) {

		// AMD. Register as an anonymous module.
		define( [ "../widgets/datepicker" ], factory );
	} else {

		// Browser globals
		factory( jQuery.datepicker );
	}
}( function( datepicker ) {

datepicker.regional.sv = {
	closeText: "Stäng",
	prevText: "&#xAB;Förra",
	nextText: "Nästa&#xBB;",
	currentText: "Idag",
	monthNames: [ "januari","februari","mars","april","maj","juni",
	"juli","augusti","september","oktober","november","december" ],
	monthNamesShort: [ "jan.","feb.","mars","apr.","maj","juni",
	"juli","aug.","sep.","okt.","nov.","dec." ],
	dayNamesShort: [ "sön","mån","tis","ons","tor","fre","lör" ],
	dayNames: [ "söndag","måndag","tisdag","onsdag","torsdag","fredag","lördag" ],
	dayNamesMin: [ "sö","må","ti","on","to","fr","lö" ],
	weekHeader: "Ve",
	dateFormat: "yy-mm-dd",
	firstDay: 1,
	isRTL: false,
	showMonthAfterYear: false,
	yearSuffix: "" };
datepicker.setDefaults( datepicker.regional.sv );

return datepicker.regional.sv;

} ) );

//checkbox icons

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