$(document).ready(() => {
    console.log("working");

    const createTable = () => {
        $('table').remove();
        const table = $('<table class="table-bordered border-primary table-striped mt-1">' +
            '<thead>' +
            '<th>ID</th><th>NAME</th><th>FRIENDS</th>' +
            '</thead>' +
            '<tbody>' +
            '</tbody>' +
            '</table>').prependTo('#table');

        $.ajax({
            url: "http://localhost:9999/listall",
            method: "GET",
            success: function (response) {
                console.log(response);
                let newResponse = [];
                for (let object of response) {
                    if (newResponse[object.id] == undefined) {
                        newResponse[object.id] = {name: "", friends: []};
                    } else {
                        newResponse[object.id].name = object.name;
                        newResponse[object.id].friends.push(object.friend);
                        newResponse[object.id].id = object.id;
                    }
                }
                console.log(newResponse);
                for (let person of newResponse) {
                    if (person != undefined && person.name) {
                        console.log(person);
                        let id = `<td class="id" rowspan='${person.friends.length}' style="">${person.id}</td>`;
                        let name = `<td rowspan='${person.friends.length}'>${person.name}</td>`;
                        let row = "<tr>" + id + name + `<td class='friend'>${person.friends[0]}</td>` + "</tr>";
                        let colFriends;
                        if (person.friends.length > 1) {
                            for (let i = 1; i < person.friends.length; ++i) {
                                colFriends += `<tr><td class='friend'>${person.friends[i]}</td></tr>`;
                            }
                        }
                        row += colFriends;
                        console.log(row);
                        $(row).appendTo('tbody');
                        $('td').css('text-align', 'center');
                        $('td:not(.id)').css('padding', '0 5rem');
                    }
                }
            },
            error: function (err, xhr) {
                console.log(err);
                console.log(xhr);
            }
        });
    }

    createTable();
    const submitBtn = $('.btn-primary');
    submitBtn.on('click', () => {
        let name = $('#name').val();
        let friends = $('#friends').val();
        friends = friends.replaceAll(" ", "");
        friends = friends.replaceAll(",", "*");

        // friends = friends.split(",");
        console.log(name);
        console.log(friends);

        if (name) {
            $.ajax({
                url: "http://localhost:9999/add",
                method: "POST",
                data: {
                    name: name,
                    friends: friends
                },
                success: function (response) {
                    console.log(response);
                    if (response && response.status && response.status === "success") {
                        let status = $('#status');
                        status.addClass("text-success");
                        status.html(response.body);
                        createTable();
                    }
                },
                error: function (err, xhr) {
                    console.log(err);
                    console.log(xhr);
                }
            })
        }
    });

    const createList = (object) => {
        if($('.list-group').length > 0) {
            $('.list-group').remove();
        }
        const list = $('<ul class="list-group"><ul>').prependTo('#list');
        for(let person of object) {
            let item = $(`<li class="list-group-item">${person.name} &emsp; ${person.friends} friends</li>`).appendTo(".list-group");
        }
    }

    let getBtn = $('.btn-success');
    getBtn.on('click', () => {
       let k = $('#get-popular').val();
       let url = `http://localhost:9999/popular?number=${k}`;
       $.ajax({
          url: url,
           method: "GET",
           success: function(response) {
              console.log(response);
                createList(response);
           },
           error: function (err, xhr) {
              console.log(err);
              console.log(xhr);
           }
       });
    });
});