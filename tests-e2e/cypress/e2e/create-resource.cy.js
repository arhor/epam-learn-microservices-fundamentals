describe('resources API test', () => {
    it('should create new resource', () => {
        cy.request('GET', 'http://localhost:5001/api/resources/1').as('resourceByIdRequest');

        cy.get('@resourceByIdRequest').then(response => {
            expect(response.status).to.eq(200);
        });
    });
});
