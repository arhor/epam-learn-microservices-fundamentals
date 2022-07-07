describe('resources API test', () => {
    it('should create new resource', () => {
        // given
        cy.request('GET', 'http://localhost:5001/api/resources/1').as('resourceByIdRequest');

        // when
        const apiCall = cy.get('@resourceByIdRequest');

        // then
        apiCall.then(response => {
            expect(response.status).to.eq(200);
        });
    });
});
