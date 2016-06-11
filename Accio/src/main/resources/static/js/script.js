function query(){
 var myQuery = document.getElementById('value').value;

 $.ajax({
                         type:"GET",
                         contentType : "application/json",
                         url: "/query",
                         dataType : 'json',
                         data: {"myQuery": myQuery},
                         success: function (data) {
                               alert("yay");
                         },
                         error: function (data) {
                               alert("oops");
                         }
                     });


}