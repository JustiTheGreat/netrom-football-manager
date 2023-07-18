const baseURL = "http://localhost:8090";
const gamesResultsURL = baseURL + "/games-results";
const headerForSendingJson = { "Content-type": "application/json", };

function httpRequest(obj) {
    return $.ajax({
        type: obj.requestType,
        url: obj.url,
        headers: obj.headers,
        data: obj.json,
        success: obj.onSuccessFunction,
        error: (error) => console.log(`Error ${error}`),
    });
}

function getAllGamesResults() {
    httpRequest({
        url: gamesResultsURL,
        requestType: "GET",
        onSuccessFunction: populateDataTable,
    });
}

function millisToFormattedDateAndTime(millis) {
    const date = new Date(millis);
    const formattedDateAndTime = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
    return formattedDateAndTime;
}

function populateDataTable(responseData) {
    const requests = [];
    for (let i = 0; i < responseData.length; i++) {
        const request = httpRequest({
            requestType: "GET",
            url: gamesResultsURL + "/complementary-data/" + responseData[i].id,
            onSuccessFunction: (response) => {
                responseData[i] = {
                    ...responseData[i],
                    ...response,
                    actions:
                    '<button type="button" class="btn btn-primary btn-block" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
                    '<button type="button" class="btn btn-danger btn-block" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
                };
                responseData[i].dateAndTimeInMillis = millisToFormattedDateAndTime(response.dateAndTimeInMillis);
            },
        });
        requests.push(request);
    }

    $.when.apply(this, requests).done(() => {
        $("#dataTable").bootstrapTable("destroy");
        $("#dataTable").bootstrapTable({data: responseData});
    });
}

document.addEventListener("DOMContentLoaded", getAllGamesResults());

function getObjectId(tr) {
    return tr.getAttribute("data-uniqueid");
}

function deleteRowData(tr) {
    httpRequest({
        requestType: "DELETE",
        url: gamesResultsURL + "/" + getObjectId(tr),
        success: getAllGamesResults(),
    });
}

function readFormFieldsValues(id) {
    return {
        id: id,
        goalsTeamOne: $("goalsTeamOne").val(),
        goalsTeamTwo: $("goalsTeamTwo").val(),
    };
}

function setFormFieldsValues(data) {
    $("goalsTeamOne").val(data.goalsTeamOne);
    $("goalsTeamTwo").val(data.goalsTeamTwo);
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit games result";

    const id = getObjectId(tr);
    httpRequest({
        requestType: "GET",
        url: gamesResultsURL + "/" + id,
        success: setFormFieldsValues,
    });

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = () => {
        const obj = readFormFieldsValues(id);
        return;
        httpRequest({
            type: "PUT",
            url: "http://localhost:8090/games-results/" + id,
            headers: headerForSendingJson,
            json: JSON.stringify(readFormFieldsValues(id)),
            success: getAllGamesResults,
        });
    }
    $("#form").modal("show");
}