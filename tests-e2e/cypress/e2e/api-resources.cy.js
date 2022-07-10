const apiResources = `${Cypress.env('resourceServiceUrl')}/resources`;
const apiSongs = `${Cypress.env('songServiceUrl')}/songs`;

describe('Resources API', () => {
    it('should create new resource', () => {
        cy.fixture('sample.mp3', 'binary').then((file) => {
            // given
            const blob = Cypress.Blob.binaryStringToBlob(file, 'audio/mpeg');
            const data = new FormData();

            data.append('file', blob, 'sample.mp3');

            // when
            cy.request({
                method: 'POST',
                url: apiResources,
                headers: {
                    'content-type': 'multipart/form-data',
                },
                body: data
            }).as('createResourceRequest');

            // then
            cy.get('@createResourceRequest').should((response) => {
                expect(response.status).to.be.equal(201);
                expect(response.headers).to.have.property('location').that.have.string(apiResources);

                const responseBody = JSON.parse(Cypress.Blob.arrayBufferToBinaryString(response.body));

                expect(responseBody).to.have.property('id').that.is.a('number');
            });
        });
    });
});
