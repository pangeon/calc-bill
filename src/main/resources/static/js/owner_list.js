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
                    '</tr>')
        }
    });
});