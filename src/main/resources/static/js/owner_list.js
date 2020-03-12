$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/api/db/owners"
    }).then(function(data) {
        for (var i = 0; data.length; i++) {
            $('.owner-list')
                .append(
                    '<tr>' +
                        '<td>' + data[i].id + '</td>' +
                        '<td>' + data[i].name + '</td>' +
                        '<td>' + data[i].surname + '</td>' +
                        '<td>' +
                                '<a href="api/db/owners/' + data[i].id + '/payments">Lista Płatności</a>' +
                        '</td>' +
                        '<td><a href="/">Edytuj</a></td>' +
                        '<td><a href="/">Usuń</a></td>' +
                    '</tr>')
        }
    });
});