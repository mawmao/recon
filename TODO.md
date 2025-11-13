
# TODO

overhaul:
    - [ ] Convert `HorizontalPager`-style form flow to `NavGraph`-style
        - [x] Convert `FormSection.Question` to a `NavGraph`
        - [ ] Create a shared view model for the nav graph to replace `FormSectionState`
        - [ ] Add new `SectionType` field on `Section` data class to specify which route to go
          - [ ] Add to `SectionSpec` annotation and define each section what type is it depending on its field
          - [ ] Generate the correct code
          IF `SectionType`:
              `BASIC` - [ ] The usual handling of inputs
              `DB`- [ ] Define what repositories and fetchFunctions are needed (could be a map)
              `GPS` - [ ] Define what repository and fetchFunction
        - [ ] Generate dependency provider depending on the generated forms (what repositories)
        - [ ] Create routes for different sections that need a specific behavior (e.g., `Basic`, `DB`, `GPS`)
        - 

in progress:
    - [ ] add validation on inputs (should validate per page before continue to next or review)
    - [ ] setup supabase connection
        - auth:
            - [x] persist login (while offline) 
                - [] store login data to be able to login previously logged in users
        - db:
            - [ ] check if there is already an entry of that form with that mfid when clicking on a form
            - create mappers for each form
                - [ ] field profile
                - [ ] cultural management
        - create review screen ui
        - refactor new code

think about:
    - field location in farmer profile (should it be using gps or manually input)
    - hiding keyboard when continuing pages in `QuestionPager`

issues:
    - login sometimes go invalid after not logging in for a few days
    - keyboard options of fields in same section fix 

todo:
    - create under construction dialog to identify unfinished features
    - refactor `QuestionPage` and `QuestionField`
    - add image input to form 2 and up
    - image handling
    - fix options of each form

todo(ui):
    - create confirm screen ui (should get data from local/already input forms)
    - implement ui for `FieldType.SEARCHABLE_DROPDOWN`

refactor:
    - strip events from recon vm events (the error and confirm) and let it handle the vm events only 

future:
    - handle camera permissions
    - use supabase realtime

future(ui):
    - improve transition going to camera and out
    - see password button on login field
    - show user details on settings
    - improve overall loading states for UX
    - add on press color change to buttons for UX


consider:
    - autofill on login fields
    - creating a logging class for technical logs
    - adding placeholders for each form field
    - add conditional form fields (e.g., preset npk content vs manually input)
        - add conditional options depending on a previous field's value
    - exiting early in questions screen if user pressed back twice 



