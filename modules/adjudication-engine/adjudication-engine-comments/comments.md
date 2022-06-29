# Comments

## Adding comment template
To add/edit comment templates go to in `adjudication-engine-app`
```
resources/comment-templates/{environment}
```
and edit your template inside environment folder.

Comments templates will be loaded into ae on startup of application into db.

If you want to edit comment templates on runtime do debug you have to disable cache:
```
ae.comments.shouldCacheTemplates=false
```

## Selecting set of comment templates

To select `environment` from which ae should load templates set property:
```
ae.comments.environment={environment}
```
