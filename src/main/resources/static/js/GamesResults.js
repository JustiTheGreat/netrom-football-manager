const baseURL = "http://localhost:8090";
const gamesResultsURL = baseURL + "/games-results";
const headerForSendingJson = { "Content-type": "application/json", };

document.addEventListener("DOMContentLoaded", getAllGamesResults);

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

function getObjectId(tr) {
    return tr.getAttribute("data-uniqueid");
}

function deleteRowData(tr) {
    httpRequest({
        requestType: "DELETE",
        url: gamesResultsURL + "/" + getObjectId(tr),
        onSuccessFunction: getAllGamesResults(),
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
                    '<button type="button" class="btn btn-primary btn-block actionButton" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
                    '<button type="button" class="btn btn-danger btn-block actionButton" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
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

function readFormFieldsValues() {
    return {
        goalsTeamOne: $("#goalsTeamOne").val(),
        goalsTeamTwo: $("#goalsTeamTwo").val(),
    };
}

function setFormFieldsValues(data) {
    $("#goalsTeamOne").val(data.goalsTeamOne ? data.goalsTeamOne : 0);
    $("#goalsTeamTwo").val(data.goalsTeamTwo ? data.goalsTeamTwo : 0);
}

function dataValidation(data) {
    return data.goalsTeamOne < 0 || data.goalsTeamTwo < 0
       || !Number.isInteger(Number(data.goalsTeamOne)) || !Number.isInteger(Number(data.goalsTeamTwo))
       ? "Please select a sound number of goals!\n" : "";
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit games result";

    const id = getObjectId(tr);
    httpRequest({
        requestType: "GET",
        url: gamesResultsURL + "/" + id,
        onSuccessFunction: setFormFieldsValues,
    });

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = () => {
        const data = readFormFieldsValues();
        data.id = id;

        if (dataValidation(data)) {
            alert(dataValidation(data));
            return;
        }

        httpRequest({
            requestType: "PUT",
            url: gamesResultsURL + "/" + id,
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllGamesResults,
        });
        $("#form").modal("hide");
    }
    $("#form").modal("show");
}