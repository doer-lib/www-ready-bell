# www-ready-bell

Source for the [ready-bell](https://www.ready-bell.com) marketing/docs site, published via GitHub
Pages to **https://www.ready-bell.com**.

Plain static HTML/CSS/JS — no build step, no npm, no `node_modules`. Edit files directly under
`public/`.

## Structure

```
www-ready-bell/
├── .github/
│   ├── dependabot.yml          # keeps the deploy workflow's actions up to date
│   └── workflows/
│       └── deploy.yml          # builds + deploys public/ to GitHub Pages on push to main
├── public/                     # exact GitHub Pages publish root
│   ├── CNAME                   # www.ready-bell.com
│   ├── index.html
│   ├── css/styles.css
│   ├── js/main.js              # tabs, copy-to-clipboard, mobile nav
│   └── examples/                # raw source files linked from the "View raw" links on the site
└── README.md                   # this file
```

## Local preview

```bash
python3 -m http.server 8000 --directory public
```

Then open `http://localhost:8000`.

## Content sync

The prose, protocol table, and code examples in `public/index.html` (and the raw files under
`public/examples/`) are manually ported from the `ready-bell` service repo's `README.md` and
`docs/` folder, with the demo host swapped from that repo's internal demo address to
`ready-bell.com:3137` (the live, hosted instance customers connect to — self-hosting
instructions are intentionally not published here). There is no automated sync: if the upstream
protocol or examples change, re-port them by hand.

## Deployment

Pushing to `main` triggers `.github/workflows/deploy.yml`, which uploads `public/` as a Pages
artifact and deploys it via `actions/deploy-pages`.

One-time manual setup (not doable from repo files):

1. Repo **Settings → Pages → Build and deployment → Source** must be set to **"GitHub Actions"**.
2. DNS: point `www.ready-bell.com` at this repo's Pages host with a `CNAME` record:
   ```
   www.ready-bell.com.  CNAME  doer-lib.github.io.
   ```
   (The target is always `<owner>.github.io` for whichever account/org owns this repo.)
3. After the first successful deploy, confirm Settings → Pages shows the custom domain and
   enable **"Enforce HTTPS"** once GitHub finishes provisioning the certificate.

Only the `www` subdomain is configured; the apex domain (`ready-bell.com` without `www`) is out
of scope for now.

## License

No LICENSE file yet — matches the upstream `ready-bell` service repo, which also doesn't have
one.
