/* Swedish initialisation for the jQuery UI date picker plugin. */
/* Written by Anders Ekdahl ( anders@nomadiz.se). */

$( function() {
	$.datepicker.setDefaults($.datepicker.regional['sv']);
	var dateFormat = "yy/mm/dd",
	from = $( "#from" )
		.datepicker({
		dateFormat: "yy/mm/dd",
		showAnim: "slideDown",
		defaultDate: "+1w",
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
