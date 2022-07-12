const RESOURCES_API_URL = `${Cypress.env('resourceServiceUrl')}/resources`;
const SONGS_API_URL = `${Cypress.env('songServiceUrl')}/songs`;

describe('Resources API', () => {
    it('should create new resource', () => {
        cy.fixture('sample.mp3', 'binary').then((file) => {
            // given ---------------------------------------------------------------------------------------------------
            const blob = Cypress.Blob.binaryStringToBlob(file, 'audio/mpeg');
            const data = new FormData();
            data.append('file', blob, 'sample.mp3');

            // when ----------------------------------------------------------------------------------------------------
            cy.request({
                method: 'POST',
                url: RESOURCES_API_URL,
                headers: {
                    'accept': 'application/json',
                    'content-type': 'multipart/form-data',
                },
                body: data
            }).as('createResourceRequest').then((response) => {
                if (response.status == 201) {
                    const resource = JSON.parse(Cypress.Blob.arrayBufferToBinaryString(response.body));

                    cy.request(response.headers.location).as('fetchResourceRequest');
                    cy.wait(3000); // wait in necessary since resource-processor takes some time to prepare metadata
                    cy.request(`${SONGS_API_URL}?resourceId=${resource.id}`).as('fetchSongMetaRequest');
                }
            });

            // then ----------------------------------------------------------------------------------------------------
            cy.get('@createResourceRequest').should((response) => {
                expect(response.status).to.be.equal(201);
                expect(response.headers).to.have.property('location').that.have.string(RESOURCES_API_URL);

                const resource = JSON.parse(Cypress.Blob.arrayBufferToBinaryString(response.body));

                expect(resource).to.have.property('id').that.is.a('number');
            });

            cy.get('@fetchResourceRequest').should((response) => {
                expect(response.status).to.be.equal(200);
            });

            cy.get('@fetchSongMetaRequest').should((response) => {
                expect(response.status).to.be.equal(200);
                expect(response.body).property('album').to.be.equal('YouTube Audio Library');
                expect(response.body).property('artist').to.be.equal('Kevin MacLeod');
                expect(response.body).property('length').to.be.equal('27.288000106811523');
                expect(response.body).property('name').to.be.equal('Impact Moderato');
            });
        });
    });
});
