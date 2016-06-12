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
                               alert(data);
                         },
                         error: function (data) {
                               alert("oops");
                               alert(data);
                         }
                     });


}