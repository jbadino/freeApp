'use strict';

describe('Controller Tests', function() {

    describe('Menu Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMenu, MockWeekMenu, MockProvider;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMenu = jasmine.createSpy('MockMenu');
            MockWeekMenu = jasmine.createSpy('MockWeekMenu');
            MockProvider = jasmine.createSpy('MockProvider');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Menu': MockMenu,
                'WeekMenu': MockWeekMenu,
                'Provider': MockProvider
            };
            createController = function() {
                $injector.get('$controller')("MenuDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'freeFoodApp:menuUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
