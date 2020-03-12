$(document).ready(function() {

    var id = document.getElementById("id").value;
    alert(id);

    $.ajax({
        url: "http://localhost:8080/api/db/owners/"+ id +"/payments"
    }).then(function(data) {
        for (var i = 0; data.length; i++) {
            $('.owner-payments-list')
                .append(
                    '<tr>' +
                    '<td>' + data[i].id + '</td>' +
                    '<td>' + data[i].amount + '</td>' +
                    '<td>' + data[i].kind + '</td>' +
                    '<td>' + data[i].date + '</td>' +
                    '<td><a href="/">Edytuj</a></td>' +
                    '<td><a href="/">Usuń</a></td>' +
                    '</tr>')
        }
    });
});