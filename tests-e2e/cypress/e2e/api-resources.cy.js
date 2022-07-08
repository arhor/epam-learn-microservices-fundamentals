describe('Resources API', () => {
    it('should create new resource', () => {
        cy.request('GET', 'http://localhost:5001/api/resources/1').as('getResourceById');

        cy.get('@getResourceById').then(response => {
            expect(response.status).to.eq(200);
        });
    });
});
