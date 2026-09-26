# ready-bell-www

Static site for **https://www.ready-bell.com**. Files live in `public/`.

## Deployment

Pushing to `main` runs `.github/workflows/deploy.yml`, which deploys `public/` to GitHub Pages.
Repo **Settings → Pages → Source** must be set to **"GitHub Actions"**.

## DNS

`public/CNAME` sets the custom domain. The DNS record:

```
www.ready-bell.com.  CNAME  doer-lib.github.io.
```

Enable **"Enforce HTTPS"** in Settings → Pages once the certificate is issued. Only `www` is
configured; the apex domain is not.
