$(document).ready(function() {
    $("#button").click(function (event) {
            event.preventDefault();
            var ids = $('#id').val();
            $.ajax({
                url: "http://localhost:8080/api/db/owners/"+ ids +"/payments"
            }).then(function(data) {
                for (var i = 0; data.length; i++) {
                    $('.owner-payments-list')
                        .append(
                            '<tr>' +
                            '<td>' + data[i].id + '</td>' +
                            '<td>' + data[i].amount + '</td>' +
                            '<td>' + data[i].kind + '</td>' +
                            '<td>' + data[i].date + '</td>' +
                            '</tr>')
                }
            });
            $("#button").prop("disabled", true);
    });
});


